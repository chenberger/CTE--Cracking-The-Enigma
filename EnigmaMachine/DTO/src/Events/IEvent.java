package Events;

public interface IEvent<TEventArgs> {
    void invoke(Object source, TEventArgs eventArgs);
}
