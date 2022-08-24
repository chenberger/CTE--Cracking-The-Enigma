package Events;

import java.util.ArrayList;
import java.util.List;

public class EventHandler<TEventArgs>
{
    private final List<IEvent<TEventArgs>> eventDelegateArray = new ArrayList<>();
    public void add(IEvent<TEventArgs> methodReference)
    {
        eventDelegateArray.add(methodReference);
    }
    public void remove(IEvent<TEventArgs> methodReference)
    {
        eventDelegateArray.remove(methodReference);
    }
    public void invoke(Object source, TEventArgs eventArgs)
    {
        if (eventDelegateArray.size()>0)
            eventDelegateArray.forEach(p -> p.invoke(source, eventArgs));
    }
    public void close()
    {
        if (eventDelegateArray.size()>0)
            eventDelegateArray.clear();
    }
}
