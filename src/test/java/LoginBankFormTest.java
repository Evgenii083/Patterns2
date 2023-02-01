import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class LoginBankFormTest {

    @BeforeEach
    void setUp() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1000x900";
        open("http://localhost:9999/");
    }

    @Test
    public void happyPathCase() {
        var registeredUser = DataGenerator.getRegistrationUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $x("//input[contains(@type, 'password')]").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $x("//*[contains(text(),' Личный кабинет')]").shouldBe(Condition.visible);
    }

    @Test
    public void shouldBeErrorInBlockedUserCase() {
        var registeredUser = DataGenerator.getRegistrationUser("blocked");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $x("//input[contains(@type, 'password')]").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $x("//*[contains(@class,'notification__content')]").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка!" + " " + "Пользователь заблокирован"));
    }

    @Test
    public void shouldBeErrorInNotRegisteredUserCase() {
        var notRegisteredUser = DataGenerator.getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $x("//input[contains(@type, 'password')]").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $x("//*[contains(@class,'notification__content')]").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка!" + " " + "Неверно указан логин или пароль"));
    }

    @Test
    public void shouldBeErrorInInvalidLoginCase() {
        var registeredUser = DataGenerator.getRegistrationUser("active");
        $("[data-test-id='login'] input").setValue(DataGenerator.generateLogin());
        $x("//input[contains(@type, 'password')]").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $x("//*[contains(@class,'notification__content')]").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка!" + " " + "Неверно указан логин или пароль"));
    }

    @Test
    public void shouldBeErrorInInvalidPasswordCase() {
        var registeredUser = DataGenerator.getRegistrationUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $x("//input[contains(@type, 'password')]").setValue(DataGenerator.generatePassword());
        $("[data-test-id=action-login]").click();
        $x("//*[contains(@class,'notification__content')]").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка!" + " " + "Неверно указан логин или пароль"));
    }

    @Test
    public void shouldBeErrorInMissedLoginCase() {
        var registeredUser = DataGenerator.getRegistrationUser("active");
        $x("//input[contains(@type, 'password')]").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $x("//span[contains(@class,'input__sub')]").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldBeErrorInMissedPasswordCase() {
        var registeredUser = DataGenerator.getRegistrationUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id=action-login]").click();
        $x("//span[contains(@class,'input__sub')]").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

}
