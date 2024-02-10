package com.arazadaz.dd.utilities;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.OutgoingChatMessage;

public class DDGeneralUtility {

    public static void sendChatMsg(String chatMessage, CommandSourceStack src){
        MutableComponent text = Component.literal(chatMessage);
        ChatFormatting textColor = ChatFormatting.DARK_GREEN;
        text.setStyle(text.getStyle().withColor(textColor));
        src.sendChatMessage(new OutgoingChatMessage.Disguised(text), false, ChatType.bind(ChatType.MSG_COMMAND_INCOMING, src));
    }

}
