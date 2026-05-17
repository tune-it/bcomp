package ru.ifmo.cs.components;

import java.util.ListResourceBundle;

// Key naming: engine.* (simulator core), cli.* (CLI output),
// binary.* (program loader), asm.* (assembler).
public class MessagesBundle extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private static final Object[][] contents = {
            // Engine
            {"engine.busy",    "Operation impossible: a program is running"},
            {"engine.aborted", "Operation aborted: a program is running"},

            // Program binary loader
            {"binary.corrupted",  "BcompNG: Program data is corrupted"},
            {"binary.empty.load", "BcompNG: Program is empty: load_address"},
            {"binary.empty.start","BcompNG: Program is empty: start_address"},
            {"binary.empty.body", "BcompNG: Program is empty: binary body"},

            // Assembler Program container
            {"asm.program.corrupted",      "AsmNG Program.getBinaryFormat: Program data is corrupted"},
            {"asm.program.empty.load",     "AsmNG Program.loadBinaryFormat: Program is empty: load_address"},
            {"asm.program.empty.start",    "AsmNG Program.loadBinaryFormat: Program is empty: start_address"},
            {"asm.program.empty.body",     "AsmNG Program.loadBinaryFormat: Program is empty: binary body"},
            {"asm.program.labels.unset",   "AsmNG Program.getLabelAddr: Labels are not set up"},
            {"asm.program.label.not_found","AsmNG Program.getLabelAddr: Label {0} not found"},

            // CLI banner / general
            {"cli.banner.title",     "Basic Computer Emulator. Version {0}"},
            {"cli.banner.ready",     "BComp is ready."},
            {"cli.banner.help_hint", "Use ? or help to get help"},
            {"cli.error",            "Error: {0}"},
            {"cli.unknown_cmd",      "Unknown command {0}"},
            {"cli.op_failed",        "operation failed: a program is running"},

            // CLI register / micro tables
            {"cli.regs.header.addr",        "Addr "},
            {"cli.regs.header.value",       "Valu"},
            {"cli.regs.header.mc",          "   MC    "},
            {"cli.regs.header.nzvc",        " NZVC "},
            {"cli.regs.header.addr_value",  "Addr Valu"},
            {"cli.regs.header.mp_counter",  "MPC "},
            {"cli.micro.header",            "Addr   MC       Label           Decoded"},

            // CLI clock / run mode
            {"cli.clock.label",      "Clock: {0}"},
            {"cli.run_mode.label",   "Run mode: {0}"},
            {"cli.yes",              "Yes"},
            {"cli.no",               "No"},
            {"cli.run_mode.running", "Running"},
            {"cli.run_mode.stopped", "Stopped"},

            // CLI IO
            {"cli.io.entry",         "IO{0} {1}"},

            // CLI arg errors
            {"cli.cmd.mwrite_needs_arg", "mwrite command requires an argument"},
            {"cli.cmd.flag_needs_arg",   "flag command requires an argument"},
            {"cli.cmd.sleep_needs_arg",  "sleep command requires an argument"},

            // CLI assembler
            {"cli.asm.prompt",        "Enter program text. Enter END to finish"},
            {"cli.asm.start_address", "Program starts at address {0}"},
            {"cli.asm.has_errors",    "Program contains errors"},

            // CLI help
            {"cli.help",
                "Available commands:\n"
                + "a[ddress]\t- Console operation \"Enter address\"\n"
                + "w[rite]\t\t- Console operation \"Write\"\n"
                + "r[ead]\t\t- Console operation \"Read\"\n"
                + "s[tart]\t\t- Console operation \"Start\"\n"
                + "c[continue]\t- Console operation \"Continue\"\n"
                + "ru[n]\t\t- Toggle Run/Stop mode\n"
                + "cl[ock]\t\t- Toggle clock-tick mode\n"
                + "ma[ddress]\t- Jump to microcommand\n"
                + "mw[rite] value\t- Write microcommand\n"
                + "mr[ead]\t\t- Read microcommand\n"
                + "md[ecode]\t- Decode current microcommand\n"
                + "mdecodea[ll]\t- Decode whole microprogram\n"
                + "stat[e]\t\t- Print BComp state register\n"
                + "io\t\t- Print state of all IO devices\n"
                + "io addr\t\t- Print state of specified IO device\n"
                + "io addr value\t- Write value to specified IO device\n"
                + "flag addr\t- Set ready flag of specified IO device\n"
                + "asm\t\t- Enter assembler program\n"
                + "sleep value\t- Delay between ticks when running in background\n"
                + "{exit|quit}\t- Exit emulator\n"
                + "(0000-FFFF)\t- Enter hexadecimal value into key register\n"
                + "labelname\t- Enter label address into key register"
            },
    };
}
