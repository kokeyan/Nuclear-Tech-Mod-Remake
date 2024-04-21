package at.martinthedragon.nucleartech

import at.martinthedragon.nucleartech.api.explosion.ExplosionLargeParams
import at.martinthedragon.nucleartech.explosion.Explosions
import at.martinthedragon.nucleartech.api.explosion.NuclearExplosionMk4Params
import at.martinthedragon.nucleartech.explosion.ExplosionLarge
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.TextComponent

class NTechCommands {
    companion object {
        private val explosionCommand: LiteralArgumentBuilder<CommandSourceStack> = Commands.literal("explode")
            .then(Commands.argument("power", FloatArgumentType.floatArg(1f, 1000f))
                .then(Commands.argument("nuke", IntegerArgumentType.integer(0, 1))
                    .then(Commands.argument("rad", IntegerArgumentType.integer())
                    .executes {
                        if (!it.source.hasPermission(2)) {
                            try {
                                it.source.playerOrException.sendMessage(TextComponent("You must have permission level \"2\""), it.source.playerOrException.uuid);
                            } catch (ignored: CommandSyntaxException) {}
                        } else if (IntegerArgumentType.getInteger(it, "nuke") == 0) {
                            ExplosionLarge.createAndStart(it.source.level, it.source.position, FloatArgumentType.getFloat(it, "power"), ExplosionLargeParams(cloud = true, rubble = true, shrapnel = true))
                        } else if (IntegerArgumentType.getInteger(it, "nuke") == 1) {
                            Explosions.getBuiltinDefault().createAndStart(it.source.level, it.source.position, FloatArgumentType.getFloat(it, "power"), NuclearExplosionMk4Params(hasFallout = true, extraFallout = IntegerArgumentType.getInteger(it, "rad")))
                        }
                        1
                    })))
        fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
            dispatcher.register(explosionCommand)
        }
    }
}
