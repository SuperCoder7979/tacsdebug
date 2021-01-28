package supercoder79.tacsdebug.client;

import supercoder79.tacsdebug.DebugRendererExt;
import supercoder79.tacsdebug.TacsDebugRenderer;
import supercoder79.tacsdebug.Tacsdebug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

@Environment(EnvType.CLIENT)
public class TacsdebugClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientSidePacketRegistry.INSTANCE.register(Tacsdebug.DEBUG_CHUNK_TICKETS, (packetContext, data) -> {
			ChunkPos chunkPos = new ChunkPos(data.readLong());

			int toLevel = data.readInt();
			long redTime = data.readLong();

			packetContext.getTaskQueue().execute(() -> ((DebugRendererExt) (MinecraftClient.getInstance().debugRenderer)).get().positions.put(chunkPos,
					new TacsDebugRenderer.PosEntry(chunkPos, Integer.toString(toLevel), redTime)));
		});
	}
}
