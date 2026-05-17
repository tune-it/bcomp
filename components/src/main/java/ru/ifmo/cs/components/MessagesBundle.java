package ru.ifmo.cs.components;

import java.util.ListResourceBundle;

// Key naming: engine.* (simulator core), cli.* (CLI output),
// binary.* (program loader), asm.* (assembler), gui.* (Swing UI).
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

            // GUI: console buttons
            {"gui.btn.read",     " F6 Read"},
            {"gui.btn.write",    "F5 Write"},
            {"gui.btn.setip",    "F4 Enter Address"},
            {"gui.btn.start",    "F7 Start"},
            {"gui.btn.continue", "F8 Continue"},
            {"gui.btn.tick",     "Shift+F9 Tick"},
            {"gui.btn.run",      "F9  Run"},
            {"gui.btn.stop",     "F9 Stop  "},
            {"gui.btn.runstop",  "F9 Run/Stop"},

            // GUI: app-level labels
            {"gui.app.basename",     "Basic computer"},
            {"gui.app.assembler",    "Assembler"},
            {"gui.app.error",        "Error"},
            {"gui.app.stop_running", "To compile, stop the running program"},
            {"gui.app.compile",      "Compile"},
            {"gui.app.cdev",         "CDEV"},

            // GUI: IO controllers
            {"gui.dev.output",  "CDev-1 (0x2-0x3) Controller of output device"},
            {"gui.dev.input",   "CDev-2 (0x4-0x5) Controller of input device"},
            {"gui.dev.io",      "CDev-3 (0x6-0x7) Controller of IO device"},
            {"gui.dev.ready",   "Ready"},
            {"gui.dev.label.1", "CDev 1"},
            {"gui.dev.label.2", "CDev 2"},
            {"gui.dev.label.3", "CDev 3"},
            {"gui.dev.label.4", "CDev 4"},
            {"gui.dev.label.5", "Dev 5"},
            {"gui.dev.label.6", "Dev 6"},
            {"gui.dev.label.7", "Dev 7"},
            {"gui.dev.label.8", "Dev 8"},
            {"gui.dev.label.9", "Dev 9"},

            // GUI: peripheral devices
            {"gui.io.printer", "Text printer (0xC-0xF)"},
            {"gui.io.ticker",  "Ticker (0x10-0x13)"},
            {"gui.io.ssd",     "Seven segment display (0x14-0x17)"},
            {"gui.io.kbd",     "Keyboard (0x18-0x1B)"},
            {"gui.io.numpad",  "Numpad (0x1C-0x1F)"},

            // GUI: peripheral widgets
            {"gui.io.flag.tooltip",   "Ready"},
            {"gui.io.power.on",       "On"},
            {"gui.io.printer.charset","Encoding"},
            {"gui.io.printer.delay",  "Delay"},
            {"gui.io.kbd.latrus",     "Lat/Rus"},
    };
}
