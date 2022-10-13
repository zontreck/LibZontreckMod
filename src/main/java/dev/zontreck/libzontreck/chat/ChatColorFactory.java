package dev.zontreck.libzontreck.chat;

public class ChatColorFactory {
    public class ColorBuilder
    {
        private String built;
        public void append(String s){
            built+=s;
        }
        public String build(){
            return built;
        }
        public ColorBuilder(){}
    }
    private ColorBuilder instance;
    public ChatColorFactory reset()
    {
        instance.append(ChatColor.resetChat());
        return this;
    }
    public ChatColorFactory set(ChatColor.ColorOptions option)
    {
        instance.append(ChatColor.from(option));
        return this;
    }
    
    public static ChatColorFactory MakeBuilder()
    {
        ChatColorFactory inst=new ChatColorFactory();
        inst.instance=new ChatColorFactory().new ColorBuilder();
        return inst;
    }
    private ChatColorFactory(){}

    @Override
    public String toString(){
        return instance.build();
    }
}
