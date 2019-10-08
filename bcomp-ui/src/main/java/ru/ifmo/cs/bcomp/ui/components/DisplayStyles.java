/*
 * $Id$
 */

package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.FontRenderContext;

import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.Utils;

/**
 *
 * @author Dmitry Afanasiev <KOT@MATPOCKuH.Ru>
 */
public class DisplayStyles {
	private static final FontRenderContext fr = new FontRenderContext(null, true, true);

	// Fonts
	public static final Font FONT_COURIER_PLAIN_12 = new Font("Courier New", Font.PLAIN, 12);
	//public static final Font FONT_COURIER_PLAIN_16 = new Font("Courier New", Font.PLAIN, 16);
	public static final Font FONT_COURIER_BOLD_18 = new Font("Courier New", Font.BOLD, 18);
	public static final int FONT_COURIER_BOLD_18_WIDTH =
		(int)Math.round(FONT_COURIER_BOLD_18.getStringBounds("0", fr).getWidth());
	public static final Font FONT_COURIER_BOLD_20 = new Font("Courier New", Font.BOLD, 20);
	public static final Font FONT_COURIER_BOLD_21 = new Font("Courier New", Font.BOLD, 21);
	public static final int FONT_COURIER_BOLD_21_WIDTH =
		(int)Math.round(FONT_COURIER_BOLD_21.getStringBounds("0", fr).getWidth());
	public static final Font FONT_COURIER_BOLD_23 = new Font("Courier New", Font.BOLD, 23);
	public static final int FONT_COURIER_BOLD_23_WIDTH =
		(int)Math.round(FONT_COURIER_BOLD_23.getStringBounds("0", fr).getWidth());
	public static final Font FONT_COURIER_BOLD_25 = new Font("Courier New", Font.BOLD, 25);
	public static final int FONT_COURIER_BOLD_25_WIDTH =
		(int)Math.round(FONT_COURIER_BOLD_25.getStringBounds("0", fr).getWidth());
	public static final Font FONT_COURIER_BOLD_45 = new Font("Courier New", Font.BOLD, 45);

	// Colors
	public static final Color COLOR_TEXT = Color.BLACK;
	public static final Color COLOR_ACTIVE = Color.RED;
	public static final Color COLOR_BUS = Color.GRAY;
	public static final Color COLOR_TITLE = new Color(157, 189, 165);
	public static final Color COLOR_VALUE = new Color(219, 249, 235);
	public static final Color COLOR_INPUT_TITLE = new Color(157 + 50, 189 + 50, 165 + 50);
	public static final Color COLOR_ACTIVE_INPUT = new Color(192, 0, 0);
	public static final String COLOR_ACTIVE_BIT = "<font color=\"#FF0000\">";
	public static final String COLOR_END = "</font>";
	public static final String HTML = "<html>";
	public static final String HTML_END = "</html>";

	// XXX: Need to rename and reorder all consts
	// Coordinates and dimentions
	// Frame dimentions
	public static final int PANE_WIDTH = 856;
	public static final int PANE_HEIGHT= 544;
	public static final Dimension PANE_SIZE = new Dimension(PANE_WIDTH, PANE_HEIGHT);

	// Memory cell height
	public static final int CELL_HEIGHT = 25;
	public static final int REG_HEIGHT =  CELL_HEIGHT + 3;
	// Basic view
	private static final int REG_1_WIDTH = 2 * FONT_COURIER_BOLD_23_WIDTH + 2;
	private static final int REG_3_WIDTH = 4 * FONT_COURIER_BOLD_23_WIDTH + 2;
	private static final int REG_4_WIDTH = 5 * FONT_COURIER_BOLD_23_WIDTH + 2;
	private static final int REG_5_WIDTH = 6 * FONT_COURIER_BOLD_23_WIDTH + 2;
	public static final int REG_8_WIDTH = (Utils.getBinaryWidth(8) + 1) * FONT_COURIER_BOLD_21_WIDTH + 2;
	public static final int REG_9_WIDTH = (Utils.getBinaryWidth(9) + 1) * FONT_COURIER_BOLD_21_WIDTH + 2;
	public static final int REG_11_WIDTH = (Utils.getBinaryWidth(11) + 1) * FONT_COURIER_BOLD_21_WIDTH + 2;
	public static final int REG_16_WIDTH = (Utils.getBinaryWidth(16) + 1) * FONT_COURIER_BOLD_21_WIDTH + 2;
	private static final int REG_16_HALF = REG_16_WIDTH / 2;
	private static final int REG_HEIGHT_HALF = CELL_HEIGHT + 1;
	// Bus width
	public static final int BUS_WIDTH = 4;
	public static final int ELEMENT_DELIM = 4 * BUS_WIDTH;
	 static final int ARROW = BUS_WIDTH * 3 + 1;
	// Buttons coordinates
	public static final int BUTTONS_HEIGHT = 30;
	public static final int BUTTONS_SPACE = 2;
	public static final int BUTTONS_Y = PANE_HEIGHT - BUTTONS_HEIGHT;
	// Keyboards register
	public static final int REG_KEY_X = 8;
	public static final int REG_KEY_Y = 126;
	public static final int ACTIVE_BIT_X = 21 + REG_16_WIDTH ;

	// ALU: Left input buses
	public static final int BUS_LEFT_INPUT_X1 = REG_KEY_X + REG_16_HALF;
	public static final int BUS_KEY_ALU = 470 - BUS_WIDTH - 1;
	// Accum
	public static final int REG_C_X_BV =   5 * BUS_WIDTH + 1;
	public static final int REG_ACCUM_X_BV = REG_C_X_BV + REG_1_WIDTH - 2;
	public static final int REG_ACCUM_Y_BV = BUS_KEY_ALU - ELEMENT_DELIM - REG_HEIGHT + 5;
	public static final int REG_DATA_X_BV = REG_ACCUM_X_BV+365;
	// From ALU
	public static final int FROM_ALU_X = REG_ACCUM_X_BV + 301;
	public static final int TO_ACCUM_Y = REG_ACCUM_Y_BV - 3 * BUS_WIDTH - 2;
	public static final int FROM_ALU_Y1 = TO_ACCUM_Y - 2 * BUS_WIDTH + 1;
	public static final int FROM_ALU_Y = FROM_ALU_Y1 - 4 * BUS_WIDTH;
	// ALU
	public static final int ALU_WIDTH = 181;
	public static final int ALU_HEIGHT = 90;
	public static final int ALU_Y = FROM_ALU_Y - ALU_HEIGHT - BUS_WIDTH;
	public static final int BUS_LEFT_INPUT_X = BUS_LEFT_INPUT_X1 + ALU_WIDTH / 3 + 1;
	public static final int BUS_LEFT_INPUT_DOWN = ALU_Y - ARROW - 1;
	public static final int BUS_LEFT_INPUT_UP = BUS_LEFT_INPUT_DOWN - 2 * BUS_WIDTH + 1;
	public static final int BUS_FROM_ACCUM_X = REG_C_X_BV - BUS_WIDTH - 1;
	public static final int BUS_FROM_ACCUM_Y = REG_ACCUM_Y_BV + REG_HEIGHT_HALF;
	public static final int REG_IP_X_BV = REG_ACCUM_X_BV + 6 * FONT_COURIER_BOLD_25_WIDTH;
	public static final int REG_DATA_Y_BV = 38;
	public static final int REG_IP_Y_BV = REG_DATA_Y_BV + ELEMENT_DELIM + REG_HEIGHT;
	public static final int REG_INSTR_Y_BV = REG_KEY_Y+ELEMENT_DELIM+REG_HEIGHT;
	public static final int BUS_FROM_IP_X = REG_IP_X_BV - BUS_WIDTH - 1;
	public static final int BUS_FROM_IP_Y = REG_IP_Y_BV + REG_HEIGHT/2;
	public static final int BUS_RIGHT_X1 = BUS_FROM_IP_X - ELEMENT_DELIM+260;

	public static final int BUS_RIGHT_X = REG_C_X_BV + ALU_WIDTH - ALU_WIDTH / 4 + 2;
	public static final int BUS_LEFT_X=BUS_RIGHT_X1-55;
	public static final int BUS_FROM_DATA_Y = REG_DATA_Y_BV + ELEMENT_DELIM;
	public static final int BUS_TO_DATA_Y = REG_DATA_Y_BV + REG_HEIGHT/2;
	public static final int BUS_RIGHT_TO_X = REG_ACCUM_X_BV + REG_16_WIDTH + ELEMENT_DELIM + BUS_WIDTH;
	public static final int REG_ADDR_Y_BV = REG_DATA_Y_BV + 3*ELEMENT_DELIM + 3*REG_HEIGHT+310;
	public static final int BUS_TO_ADDR_Y = REG_ADDR_Y_BV + REG_HEIGHT_HALF-300;
	public static final int BUS_TO_ADDR_X = REG_ACCUM_X_BV + REG_11_WIDTH + ARROW;
	public static final int REG_INSTR_X_BV = BUS_RIGHT_TO_X + BUS_WIDTH + ELEMENT_DELIM + 1;
	public static final int BUS_TO_INSTR_X = REG_INSTR_X_BV - ARROW - 1+360;
	public static final int BUS_TO_DATA_X = REG_ACCUM_X_BV + REG_16_WIDTH + ARROW;
	public static final int BUS_FROM_INSTR_Y = REG_ADDR_Y_BV-300;
	public static final int BUS_FROM_INSTR_X = REG_INSTR_X_BV + 2 * ELEMENT_DELIM + BUS_WIDTH;
	public static final int MEM_X = 720;
	public static final int MEM_Y = REG_DATA_Y_BV;
	private static final int MEM_WIDTH = 9 * FONT_COURIER_BOLD_25_WIDTH + 3;
	public static final int BUS_ADDR_X1 = BUS_TO_INSTR_X;
	public static final int BUS_ADDR_X2 = MEM_X + MEM_WIDTH + ARROW;
	public static final int BUS_READ_Y = REG_DATA_Y_BV + CELL_HEIGHT / 2 + 1;
	public static final int BUS_WRITE_Y = BUS_READ_Y + REG_HEIGHT_HALF;
	public static final int BUS_READ_X1 = REG_ACCUM_X_BV - ARROW - 1;
	public static final int BUS_READ_X2 = MEM_X + MEM_WIDTH + BUS_WIDTH;
	public static final int CYCLEVIEW_Y = BUS_LEFT_INPUT_UP + BUS_WIDTH + ELEMENT_DELIM + 1;
	public static final int BUS_INSTR_TO_CU_X = REG_INSTR_X_BV + REG_16_HALF;
	public static final int BUS_INSTR_TO_CU_Y = CYCLEVIEW_Y - ARROW - 1;

	// IO view
	private static final int REGS_RIGHT_X = MEM_X + MEM_WIDTH + REG_8_WIDTH + ELEMENT_DELIM - 1;
	public static final int CU_X_IO = REGS_RIGHT_X - REG_8_WIDTH;
	public static final int REG_ACC_X_IO = CU_X_IO + REG_1_WIDTH - 1;
	public static final int CU_Y_IO = MEM_Y + ELEMENT_DELIM;
	public static final int REG_ADDR_Y_IO = CU_Y_IO + REG_HEIGHT + ELEMENT_DELIM;
	public static final int REG_IP_Y_IO = REG_ADDR_Y_IO + REG_HEIGHT + ELEMENT_DELIM;
	public static final int REG_DATA_Y_IO = REG_IP_Y_IO + REG_HEIGHT + ELEMENT_DELIM;
	public static final int REG_INSTR_Y_IO = REG_DATA_Y_IO + REG_HEIGHT + ELEMENT_DELIM;
	public static final int REG_ACCUM_Y_IO = REG_INSTR_Y_IO + REG_HEIGHT + ELEMENT_DELIM;
	public static final int IO_DELIM = REG_8_WIDTH + ELEMENT_DELIM;
	public static final int IO_X = REG_INSTR_X_BV + REG_16_WIDTH - REG_8_WIDTH - 2 * IO_DELIM;
	public static final int FLAG_WIDTH = 100;
	public static final int FLAG_OFFSET = (REG_8_WIDTH - FLAG_WIDTH) / 2;
	public static final int BUS_INTR_Y = CU_Y_IO + CELL_HEIGHT / 2 + 1;
	public static final int LABEL_INTR_Y = BUS_INTR_Y - ELEMENT_DELIM - 2 * BUS_WIDTH;
	public static final int BUS_INTR_LEFT_X = REGS_RIGHT_X + ARROW;
	public static final int IO1_CENTER = IO_X + REG_8_WIDTH / 2;
	public static final int IO2_CENTER = IO1_CENTER + IO_DELIM;
	public static final int IO3_CENTER = IO2_CENTER + IO_DELIM;
	public static final int BUS_INTR_Y1 = BUS_INTR_Y + ELEMENT_DELIM;
	public static final int FLAG_Y = BUS_INTR_Y1 + BUS_WIDTH + 1;
	public static final int BUS_TSF_X1 = IO_X - ELEMENT_DELIM - BUS_WIDTH - 1 + FLAG_OFFSET;
	public static final int BUS_TSF_Y2 = FLAG_Y + CELL_HEIGHT + BUS_WIDTH;
	public static final int BUS_TSF_Y = BUS_TSF_Y2 + ELEMENT_DELIM;
	public static final int BUS_TSF_Y1 = CU_Y_IO + REG_HEIGHT + ARROW - 1;
	public static final int BUS_TSF_X = CU_X_IO + REG_8_WIDTH - ELEMENT_DELIM - 1;
	public static final int LABEL_TSF_Y = BUS_TSF_Y + 2 * BUS_WIDTH;
	// XXX: Hardcoded const for IO address buses
	public static final int LABEL_ADDR_Y = LABEL_TSF_Y + ELEMENT_DELIM + 4;
	public static final int BUS_IO_ADDR_Y = LABEL_ADDR_Y + ELEMENT_DELIM + BUS_WIDTH + 1;
	public static final int DECODER_Y = BUS_IO_ADDR_Y + ELEMENT_DELIM + BUS_WIDTH + 1;
	public static final int BUS_IO_ADDR_Y1 = DECODER_Y - ARROW - 1;
	public static final int BUS_IO_ADDR_Y2 = REG_DATA_Y_IO + CELL_HEIGHT / 2 + 1;
	public static final int BUS_IO_ADDR_X = CU_X_IO + REG_4_WIDTH + BUS_WIDTH;
	private static final int DECODER_HEIGHT = 3 * CELL_HEIGHT + 2;
	public static final int BUS_IO_REQ_Y = DECODER_Y + DECODER_HEIGHT + ELEMENT_DELIM + BUS_WIDTH;
	public static final int BUS_IO_REQ_Y1 = DECODER_Y + DECODER_HEIGHT + ARROW;
	public static final int LABEL_REQ_Y = BUS_IO_REQ_Y + 2 * BUS_WIDTH;
	public static final int LABEL_IN_Y = LABEL_REQ_Y + ELEMENT_DELIM;
	public static final int BUS_IN_Y = LABEL_IN_Y + ELEMENT_DELIM + BUS_WIDTH + 1;
	public static final int IO_DATA_Y = BUS_IN_Y + BUS_WIDTH + ELEMENT_DELIM;
	public static final int BUS_IN_Y1 = REG_ACCUM_Y_IO + CELL_HEIGHT / 2 + 1;
	public static final int BUS_IN_Y2 = IO_DATA_Y - BUS_WIDTH - 1;
	public static final int BUS_IN_X = REG_ACC_X_IO + REG_4_WIDTH + ARROW;
	public static final int BUS_OUT_Y = BUS_IN_Y1 + REG_HEIGHT_HALF;
	public static final int BUS_OUT_Y2 = IO_DATA_Y + REG_HEIGHT + ARROW;
	public static final int BUS_OUT_X = REG_ACC_X_IO + REG_4_WIDTH + BUS_WIDTH;
	public static final int LABEL_OUT_Y = BUS_OUT_Y + 2 * BUS_WIDTH;

	// MPView
	// XXX: Hardcode
	private static final int MP_REGS_WIDTH = REG_4_WIDTH + ELEMENT_DELIM + REG_5_WIDTH;
	public static final int ALU_X_MP = CU_X_IO + (MP_REGS_WIDTH - ALU_WIDTH) / 2;
	public static final int REG_ACC_Y_MP = REG_IP_Y_IO + ALU_HEIGHT + ELEMENT_DELIM;
	public static final int REG_STATE_Y_MP = REG_ACC_Y_MP + REG_HEIGHT + ELEMENT_DELIM;
	private static final int REG_RIGHT_X_MP = CU_X_IO + MP_REGS_WIDTH;
	public static final int REG_IP_X_MP = REG_RIGHT_X_MP - REG_3_WIDTH;
	public static final int REG_INSTR_X_MP = REG_RIGHT_X_MP - REG_4_WIDTH;
	public static final int REG_BUF_X_MP = REG_RIGHT_X_MP - REG_5_WIDTH;
	public static final int REG_STATE_X = CU_X_IO + (MP_REGS_WIDTH - REG_9_WIDTH) / 2;
	private static final int LEFT_X = IO_X + 3 * IO_DELIM - 1;
	public static final int MICROMEM_X = LEFT_X - MEM_WIDTH;

	// Assembler view
	public static final int TEXTAREA_X = 1;
	public static final int TEXTAREA_Y = 1;
	public static final int TEXTAREA_WIDTH = 600;
	public static final int TEXTAREA_HEIGHT = PANE_HEIGHT - 2;
}
