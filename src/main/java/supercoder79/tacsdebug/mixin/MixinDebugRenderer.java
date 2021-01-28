package supercoder79.tacsdebug.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.tacsdebug.DebugRendererExt;
import supercoder79.tacsdebug.TacsDebugRenderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(DebugRenderer.class)
public class MixinDebugRenderer implements DebugRendererExt {
	private TacsDebugRenderer tacsDebugRenderer;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void handleConstructor(MinecraftClient client, CallbackInfo ci) {
		this.tacsDebugRenderer = new TacsDebugRenderer();
	}

	@Inject(method = "render", at = @At("HEAD"))
	private void handleRender(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
		tacsDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
	}

	@Override
	public TacsDebugRenderer get() {
		return tacsDebugRenderer;
	}
}