package net.unit8.sigcolle.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by hondayoshitaka on 2017/02/18.
 */
@Data
public class CreateCampaignForm extends FormBase {
    @NotNull
    @Length(max = 30)
    private String title;

    @NotNull
    @Length(max = 1000)  // システム的に確からしい数字
    private  String statement;

    @NotNull
    private Long goal;
}
