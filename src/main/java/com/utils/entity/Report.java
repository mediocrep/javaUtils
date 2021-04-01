package com.utils.entity;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.*;


/**
 * @NoArgsConstructor will generate a constructor with no parameters.
 *
 * @RequiredArgsConstructor generates a constructor with 1 parameter for each field that requires special handling.
 * All non-initialized final fields get a parameter, as well as any fields that are marked as @NonNull that aren't
 * initialized where they are declared. For those fields marked with @NonNull, an explicit null check is also generated.
 * The constructor will throw a NullPointerException if any of the parameters intended for the fields marked with
 * @NonNull contain null. The order of the parameters match the order in which the fields appear in your class.
 *
 * @AllArgsConstructor generates a constructor with 1 parameter for each field in your class. Fields marked with
 * @NonNull result in null checks on those parameters.
 */
@Data
@NoArgsConstructor(staticName = "getNoArgsInstance")
@AllArgsConstructor
//@RequiredArgsConstructor
public class Report {
    private Integer id;
    private String no;
    private String reportType;
    private String reportTypeContent;
    private String reportName;

    public Report(String no, String reportType, String reportTypeContent) {
        this.no = no;
        this.reportType = reportType;
        this.reportTypeContent = reportTypeContent;
    }
}
