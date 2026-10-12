package com.wcc.recycling_centre.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateWasteTypeForm {

    @NotBlank(message = "Waste type name is required")
    private String name;

    @NotBlank(message = "Waste type code is required")
    private String code;
}