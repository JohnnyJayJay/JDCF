/*
    Copyright (c) 2019 Ivan Pekov
    Copyright (c) 2019 Contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/
package com.mrivanplays.jdcf.args;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class containing the default argument resolvers.
 */
public final class ArgumentResolvers
{

    public static ArgumentResolver<Integer> INTEGER = context -> Integer.parseInt(context.getArgument());
    public static ArgumentResolver<Double> DOUBLE = context -> Double.parseDouble(context.getArgument());
    public static ArgumentResolver<Float> FLOAT = context -> Float.parseFloat(context.getArgument());

    public static ArgumentResolver<User> USER_MENTION = new ArgumentResolver<User>()
    {

        private final Pattern UM_PATTERN = Pattern.compile("<@!?(\\d{17,20})>");

        @Override
        public User resolve(@NotNull ArgumentResolverContext context) throws Exception
        {
            Matcher matcher = UM_PATTERN.matcher(context.getArgument());
            if (matcher.matches())
            {
                User user = context.getJda().getUserById(matcher.group(1));
                if (user == null)
                {
                    // represent that the value present wasn't a mention
                    throw new IllegalArgumentException();
                } else
                {
                    return user;
                }
            } else
            {
                // represent that the value wasn't a mention
                throw new IllegalArgumentException();
            }
        }
    };

    public static ArgumentResolver<User> USER_ID = context -> {
        User user = context.getJda().getUserById(Long.parseLong(context.getArgument()));
        if (user == null)
        {
            throw new IllegalArgumentException();
        } else
        {
            return user;
        }
    };

    public static ArgumentResolver<User> USER = context -> {
        try
        {
            return USER_ID.resolve(context);
        }
        catch (Exception e)
        {
            return USER_MENTION.resolve(context);
        }
    };
}
