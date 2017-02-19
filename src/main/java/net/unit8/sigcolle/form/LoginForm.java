package net.unit8.sigcolle.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import net.unit8.sigcolle.validator.Password;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author takahashi
 */
@Data
public class LoginForm extends FormBase {
    @NotBlank
    @Length(max = 50)
    @Email
    private String email;

    @NotBlank
    @Length(min = 4, max = 20)
    @Password
    private String pass;
}
