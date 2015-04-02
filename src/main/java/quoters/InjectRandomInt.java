package quoters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SOURCE видна только в исходном коде, когда компилируется в байткод уже ничего не будет (пример @Override)
 * CLASS в байт код попасть должна, но при этом все равно через reflection в runtime считать нельзя ее там не будет
 * RUNTIME видна в рантайме, которую через рефлекшен считать можно
 * @author zheka
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectRandomInt {
	int min();
	int max();
}