package quoters;

import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {
	private Map<String, Class> map = new HashMap<String, Class>();
	// То что этот контроллер создан в формате MBean, он еще не зарегистрирован в MBeanServer
	private ProfilingController controller = new ProfilingController();
	
	public ProfilingHandlerBeanPostProcessor() throws Exception {
		// Регистрация MBean в MBeanServer
		MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
		platformMBeanServer.registerMBean(controller, new ObjectName("profileing", "name", "controller"));
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<?> beanClass = bean.getClass();
		if (beanClass.isAnnotationPresent(Profiling.class)){
			map.put(beanName, beanClass);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class beanClass = map.get(beanName);
		if (beanClass != null) {
			// генерируем объект с помощью DynamicProxy
			// Статический метод newProxyInstance создает объект из нового класса, который он сам же сгенеририрует на лету
			// 1 параметр. ClassLoader при помощи которого класс, который сгенерируется на лету загрузиться в heap 
			// 2 параметр. Спискок интерфейсов, который должен имплементировать тот класс, который сгенерируется на лету
			// Создается новый класс, который должен имплементировать те же интерфейсы, которые имплементирует оригинальный класс бина
			// 3 параметр. InvocationHandler это объект, который будет инкапсулировать логику, которая попадет во все методы класса, который
			// сгенериться на лету
			return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					if (controller.isEnabled()) {
						System.out.println("PROFILING");
						long before = System.nanoTime();
						Object retVal = method.invoke(bean, args);
						long after = System.nanoTime();
						System.out.println(after - before);
						System.out.println("END");
						return retVal;
					} else {
						return method.invoke(bean,  args);
					}
				}
			});
		}
		return bean;
	}
}