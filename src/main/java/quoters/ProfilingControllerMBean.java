package quoters;

/**
 * В этом интерфейсе мы указываем те методы, которые мы хотим чтобы были доступны через JMXConsole
 * @author zheka
 *
 */
public interface ProfilingControllerMBean {
	public void setEnabled(boolean enabled);
}