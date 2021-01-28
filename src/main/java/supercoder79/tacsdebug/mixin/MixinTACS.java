package supercoder79.tacsdebug.mixin;

import io.netty.buffer.Unpooled;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import supercoder79.tacsdebug.Tacsdebug;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

@Mixin(ThreadedAnvilChunkStorage.class)
public class MixinTACS {

	@Shadow @Final private ServerWorld world;

	@Inject(method = "setLevel", at = @At("HEAD"))
	public void handleLevel(long pos, int level, ChunkHolder holder, int i, CallbackInfoReturnable<ChunkHolder> cir) {
		for (ServerPlayerEntity player : this.world.getPlayers()) {
			sendChunkTicketData(player, pos, level);
		}
	}

	private static  void sendChunkTicketData(PlayerEntity player, long pos, int level) {
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());

		data.writeLong(pos);
		data.writeInt(level);
		data.writeLong(System.currentTimeMillis() + 2000);

		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, Tacsdebug.DEBUG_CHUNK_TICKETS, data);
	}
}
