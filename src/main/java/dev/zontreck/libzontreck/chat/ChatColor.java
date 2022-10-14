package dev.zontreck.libzontreck.chat;

public class ChatColor {
    public enum ColorOptions{
        Black,
        Dark_Blue,
        Dark_Green,
        Dark_Aqua,
        Dark_Red,
        Dark_Purple,
        Gold,
        Gray,
        Dark_Gray,
        Blue,
        Green,
        Aqua,
        Red,
        Light_Purple,
        Yellow,
        White,
        MinecoinGold,
        Underline,
        Bold,
        Italic,
        Strikethrough,
        Crazy,
        Reset
    }
    public static char CODE = '§';
    public static String BLACK = build("0");
    public static String DARK_BLUE = build("1");
    public static String DARK_GREEN = build("2");
    public static String DARK_AQUA = build("3");
    public static String DARK_RED = build("4");
    public static String DARK_PURPLE = build("5");
    public static String GOLD = build("6");
    public static String GRAY = build("7");
    public static String DARK_GRAY = build("8");
    public static String BLUE = build("9");
    public static String GREEN = build("a");
    public static String AQUA = build("b");
    public static String RED = build("c");
    public static String LIGHT_PURPLE = build("d");
    public static String YELLOW = build("e");
    public static String WHITE = build("f");
    public static String MINECOIN_GOLD = build("g");

    public static String UNDERLINE = build("u");
    public static String BOLD = build("l");
    public static String ITALIC = build("o");
    public static String STRIKETHROUGH = build("m");
    public static String CRAZY = build("k");
    public static String RESET = build("r");

    public static String doColors(String msg)
    {
        for(ChatColor.ColorOptions color : ChatColor.ColorOptions.values()){
            msg = msg.replace("!"+color.toString()+"!", ChatColor.from(color));
            msg = msg.replace("!"+color.toString().toLowerCase()+"!", ChatColor.from(color));
        }
        for(ChatColor.ColorOptions color : ChatColor.ColorOptions.values()){
            String correctCode = ChatColor.from(color);
            String searchCode = "&"+correctCode.substring(1);
            msg = msg.replace(searchCode, correctCode);
        }
        return msg;
    }

    public static String build(String c)
    {
        return CODE+c;
    }

    public static String resetChat()
    {
        return RESET+WHITE;
    }

    public static String from(ColorOptions nick){
        switch(nick){
            case Black:
            {
                return BLACK;
            }
            case Dark_Blue:
            {
                return DARK_BLUE;
            }
            case Dark_Green:
            {
                return DARK_GREEN;
            }
            case Dark_Aqua:
            {
                return DARK_AQUA;
            }
            case Dark_Red:
            {
                return DARK_RED;
            }
            case Dark_Purple:
            {
                return DARK_PURPLE;
            }
            case Gold:
            {
                return GOLD;
            }
            case Gray:
            {
                return GRAY;
            }
            case Dark_Gray:
            {
                return DARK_GRAY;
            }
            case Blue:
            {
                return BLUE;
            }
            case Green:
            {
                return GREEN;
            }
            case Aqua:
            {
                return AQUA;
            }
            case Red:
            {
                return RED;
            }
            case Light_Purple:
            {
                return LIGHT_PURPLE;
            }
            case Yellow:
            {
                return YELLOW;
            }
            case White:
            {
                return WHITE;
            }
            case MinecoinGold:
            {
                return MINECOIN_GOLD;
            }
            case Underline:
            {
                return UNDERLINE;
            }
            case Bold:
            {
                return BOLD;
            }
            case Italic:
            {
                return ITALIC;
            }
            case Strikethrough:
            {
                return STRIKETHROUGH;
            }
            case Crazy:
            {
                return CRAZY;
            }
            case Reset:
            {
                return RESET+WHITE;
            }
            default:
            {
                return RESET+CRAZY;
            }
        }
    }
}
