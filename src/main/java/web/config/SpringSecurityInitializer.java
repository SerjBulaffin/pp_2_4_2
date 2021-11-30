package web.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    //пустой класс, использующийся для резистрации модуля в спринг-контейнере
}
/*
- SpringSecurityInitializer - обязателен для не boot-приложения. Кода в нем нет, но требуется для регистрации секьюрити
    в Спринг-контейнере.
 */