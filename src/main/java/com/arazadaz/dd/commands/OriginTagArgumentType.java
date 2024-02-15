package com.arazadaz.dd.commands;

import com.arazadaz.dd.api.origins.OriginManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class OriginTagArgumentType implements ArgumentType<String> {

    public static OriginTagArgumentType tag(){
        return new OriginTagArgumentType();
    }

    @Override
    public String parse(StringReader stringReader) throws CommandSyntaxException {
        return stringReader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> pContext, SuggestionsBuilder pBuilder) {
        if (!(pContext.getSource() instanceof SharedSuggestionProvider)) {
            return Suggestions.empty();
        } else {
            String s = pBuilder.getRemaining();
            Collection<String> collection = OriginManager.knownTags;

            for(String s1 : collection) {
                    pBuilder.suggest(s1);
            }

            return pBuilder.buildFuture();
        }


    }

}
