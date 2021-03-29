package com.tyr.xdeveloper.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AbsentView {

    private String todayAbsent;

    private String tooMuchAbsent;

}
