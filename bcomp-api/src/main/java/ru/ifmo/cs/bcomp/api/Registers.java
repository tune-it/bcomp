package ru.ifmo.cs.bcomp.api;

import static ru.ifmo.cs.bcomp.api.Utils.hexa;
import static ru.ifmo.cs.bcomp.api.Utils.hexv;
import static ru.ifmo.cs.components.Utils.toHex;

/**
 *
 * @author serge
 */
public class Registers {

    /** All registeres, including mpu 
     */
    public long id=0L,mppre=0L,mpafter=0L,
            ar=0L,br=0L,cr=0L,dr=0L,acc=0L,sp=0L,
            bitn=0L,bitz=0L,bitv=0L,bitc=0L,ip = 0L;

    /** memory access information
      * mop: 1 - load; 2 - store; -1 no memory info; 0 - no op, just store 
      */
    public long mop=-1L,maddr=0L,mval=0L;
    /** Assembler mnemonic, filled in compileAndExecuteProgram
     */
    public String mmnemo = "";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Registers{");
        sb.append("mppre=0x").append(toHex(mppre, 8));
        sb.append(", mop=").append(mop);
        sb.append(", maddr=0x").append(hexa(maddr));
        sb.append(", mval=0x").append(hexv(mval));
        sb.append(", mmnemo=").append(mmnemo);
        sb.append(", ip=0x").append(hexa(ip));
        sb.append(", ar=0x").append(hexa(ar));
        sb.append(", br=0x").append(hexv(br));
        sb.append(", cr=0x").append(hexv(cr));
        sb.append(", dr=0x").append(hexv(dr));
        sb.append(", acc=0x").append(hexv(acc));
        sb.append(", sp=0x").append(hexv(sp));
        sb.append(", bitn=").append(bitn);
        sb.append(", bitz=").append(bitz);
        sb.append(", bitv=").append(bitv);
        sb.append(", bitc=").append(bitc);
        sb.append(", mpafter=0x").append(toHex(mpafter, 8));
        sb.append('}');
        return sb.toString();
    }
    
    public String toTraceTab(boolean mpu) {
        if (mpu) return toTraceTabMPU();
        return toTraceTabBase();
    }

    private String toTraceTabBase() {
        StringBuilder sb = new StringBuilder();
        if (mop >=0 ) {
            sb.append("*  ").append(hexa(maddr)).append("  * ");
            if (mmnemo.isEmpty()) 
                sb.append("     ").append(hexv(mval)).append("      ");
            else 
                sb.append(" ").append(mmnemo).append("     ");
        }
        else {
            sb.append("*       *                ");
        }
        sb.append(" * ").append(hexa(ip));
        sb.append(" *  ").append(hexv(cr));
        sb.append(" *  ").append(hexa(ar));
        sb.append(" *  ").append(hexv(dr));
        sb.append(" *  ").append(hexv(br));
        sb.append(" *  ").append(hexv(acc));
        sb.append(" * ")
                .append(bitn != 0 ? '1' : '0')
                .append(bitz != 0 ? '1' : '0')
                .append(bitv != 0 ? '1' : '0')
                .append(bitc != 0 ? '1' : '0');
        sb.append(" *");
        return sb.toString();
    }
    
    private String toTraceTabMPU() {
        StringBuilder sb = new StringBuilder();
        sb.append("*  ").append(toHex(mppre, 8));
        if (mop < 0 ) {
            sb.append("  *       *         ");
        } else {
            String op = "   ";
            switch ((int)mop) {
                case 1: op = "(L)"; break;
                case 2: op = "(S)"; break;
                default:
            }
            sb.append("  *  ").append(hexa(maddr)).append("  *  ")
              .append(hexv(mval)).append(op);
        }
        sb.append(" * ").append(hexa(ip));
        sb.append(" *  ").append(hexv(cr));
        sb.append(" *  ").append(hexa(ar));
        sb.append(" *  ").append(hexv(dr));
        sb.append(" *  ").append(hexv(br));
        sb.append(" *  ").append(hexv(acc));
        sb.append(" * ")
                .append(bitn != 0 ? '1' : '0')
                .append(bitz != 0 ? '1' : '0')
                .append(bitv != 0 ? '1' : '0')
                .append(bitc != 0 ? '1' : '0');
        sb.append(" *  ").append(ru.ifmo.cs.components.Utils.toHex(mpafter, 8));
        sb.append("  *");
        return sb.toString();
    }

    private static final String HEADER_MPU =
        "--------------------------------------------------------------------------------------\n"+
        "* CчМК * Память чт./зап.  *   Содержимое регистров CPU после исполнения микрокоманды *\n"+
        "*до выб* Адрес * Значение *  IP *   CR  *  AR  *   DR  *   BR  *   AC  * NZVC * СчМК *\n"+
        "--------------------------------------------------------------------------------------\n";
    
    private static final String HEADER =
        "-------------------------------------------------------------------------------\n"+
        "* Aдрес *  Команда/данные *  IP *   CR  *  AR  *   DR  *   BR  *   AC  * NZVC *\n"+
        "-------------------------------------------------------------------------------\n";
    
    public static String traceTabHeader (boolean mpu) {
        if (mpu==true) return HEADER_MPU;
        return HEADER;
    }

}
