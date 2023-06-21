package webProject.togetherPartyTonight.global.common;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* 해당 annotation이 실행 할 ConstraintValidator 구현체를 `EnumValidator`로 지정합니다. */
@Constraint(validatedBy = {EnumValidator.class})
/* 해당 annotation은 메소드, 필드, 파라미터에 적용 할 수 있습니다. */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
/* annotation을 Runtime까지 유지합니다. */
@Retention(RetentionPolicy.RUNTIME)
public @interface Enum {
    String message() default "허용되지 않은 카테고리 이름입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends java.lang.Enum<?>> enumClass();
    boolean ignoreCase() default false;
}
