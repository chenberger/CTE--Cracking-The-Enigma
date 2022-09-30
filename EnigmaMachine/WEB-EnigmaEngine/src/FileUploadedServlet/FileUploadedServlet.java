package FileUploadedServlet;

import Engine.JaxbToMachineTransformer;
import EnigmaMachineException.*;
import Jaxb.Schema.Generated.CTEEnigma;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import servletUtils.ServletUtils;
import servletUtils.SessionUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "FileUploadedServlet", urlPatterns = "/fileUploaded")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class FileUploadedServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String name = SessionUtils.getUsername(request) != null ? SessionUtils.getUsername(request) : "Guest";
        Collection<Part> parts = request.getParts();
        for(Part part : parts) {
            InputStream inputStream = part.getInputStream();
            JaxbToMachineTransformer jaxbToMachineTransformer = new JaxbToMachineTransformer();
            try {
                CTEEnigma cteEnigma = jaxbToMachineTransformer.deserializeFrom(inputStream);
                if(ServletUtils.getUBoatManager(request.getServletContext()).isFileExists(cteEnigma.getCTEBattlefield().getBattleName())){
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    out.print("File is already uploaded" + " by: "+ ServletUtils.getUBoatManager(request.getServletContext()).getUBoatByBattleName(cteEnigma.getCTEBattlefield().getBattleName()));
                    out.flush();

                }
                else {
                    ServletUtils.getEngineManager(request.getServletContext()).setMachineDetailsFromXmlFile(cteEnigma);

                    ServletUtils.getUBoatManager(request.getServletContext()).addUBoat(SessionUtils.getUsername(request), ServletUtils.getEngineManager(request.getServletContext()).getCurrentEnigmaMachine()
                    , cteEnigma.getCTEBattlefield());
                    response.setStatus(HttpServletResponse.SC_OK);

                    out.println("File uploaded successfully!");
                }
            }
             catch (GeneralEnigmaMachineException | JAXBException | IllegalAgentsAmountException |
                    MachineNotExistsException | CloneNotSupportedException | BruteForceInProgressException e) {
                out.println(e.getMessage());
            }
        }
    }
}
