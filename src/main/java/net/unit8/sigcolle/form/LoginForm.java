package net.unit8.sigcolle.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import net.unit8.sigcolle.validator.Password;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * @author takahashi
 */
@Data
public class LoginForm extends FormBase {

    @NotNull
    @Length(min = 1, max = 50)
    @Email
    private String email;

    @NotNull
    @Length(min = 4, max = 20)
    @Password
    private String pass;

    @Override
    public boolean hasErrors() {
        return super.hasErrors();
    }

    @Override
    public boolean hasErrors(String name) {
        return super.hasErrors(name);
    }

    @Override
    public List<String> getErrors(String name) {
        return super.getErrors(name);
    }
}
