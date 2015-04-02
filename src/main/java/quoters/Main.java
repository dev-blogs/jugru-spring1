package quoters;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String [] args) throws Exception {
		// BeanFactory который отвечает за создание и хранение всех объектов, которые singleton
		// Имплементация вот этого контекста она сканируется и анализируется XmlBeanDefinitionReader		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		//context.getBean(Quoter.class).sayQuote();
	}
}