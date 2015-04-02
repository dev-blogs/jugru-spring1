package quoters;

import javax.annotation.PostConstruct;

/**
 * Будет BeanPostProcessor, который будет к этой аннотации относится
 * то есть он будет получать bean от BeanFactory, он будет спрашивать а не стоит ли над классом этого бина аннотация Profiling
 * и если стоит, то ему придется делать очень солжную работу, ему придется в каждый метод данного бина дописывать логику
 * связанную с профайлингом.
 * BeanPostProcessor получает класс, добавляет ему логику связанную с профилированием, для этого он создает новый класс,
 * в котором он делегирует вызовы в существующие методы, и добавляет нужную логику, и потом из этого класса создается объект,
 * и эта полученная декарация или прокси возвращется обратно в BeanFactory
 * Есть два варианта:
 * 1. Он должен наследоваться от оригинального класса и переопределять его методы и добавляя туда нужную логику
 * Этот подоход называется CGLIB
 * 2. Он должен имплементировать те же самые интерфейсы
 * Этот подоход называется DymamicProxy 
 * @author zheka
 *
 */
@Profiling
public class TerminatorQuoter implements Quoter {
	@InjectRandomInt(min = 2, max = 7)
	private int repeat;
	private String message;
	
	public TerminatorQuoter() {
		System.out.println("Phase 1");
		System.out.println(repeat);
	}
	
	@PostConstruct
	public void init() {
		System.out.println("Phase 2");
		System.out.println(repeat);
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@PostProxy
	public void sayQuote() {
		System.out.println("Phase 3");
		for (int i = 0; i < repeat; i++) {
			System.out.println(message);
		}
	}
}