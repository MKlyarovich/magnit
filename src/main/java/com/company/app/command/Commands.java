package com.company.app.command;

import com.company.app.service.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.validation.constraints.Min;

@ShellComponent
@RequiredArgsConstructor
public class Commands {

    private final CalculationService calculationService;

    @ShellMethod("Calculate sum")
    public long sum(@Min(1) int n) {
        return calculationService.calculate(n);
    }
}