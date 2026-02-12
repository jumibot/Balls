package engine.controller.mappers;

// region Imports
import java.util.ArrayList;

import engine.model.bodies.ports.BodyData;
import engine.model.physics.ports.PhysicsValuesMDTO;
import engine.utils.pooling.Pool;
import engine.view.renderables.ports.DynamicRenderDTO;
// endregion

public class DynamicRenderableMapper extends AbstractPooledMapper<DynamicRenderDTO> {

    // region Constructors
    public DynamicRenderableMapper() {
        super();
    }
    // endregion Constructors

    @Override
    protected Pool<DynamicRenderDTO> createPool() {
        return new Pool<>(() -> new DynamicRenderDTO(null, 0, 0, 0, 0, 0L, 0, 0, 0, 0, 0L));
    }

    // *** PUBLIC ***

    public DynamicRenderDTO fromBodyDTOPooled(BodyData bodyData) {
        DynamicRenderDTO dto = this.map(bodyData);
        if (dto == null) {
            System.out.println("[LOG] DynamicRenderableMapper: map returned null for BodyData: " + bodyData);
        }
        return dto;
    }

    public ArrayList<DynamicRenderDTO> fromBodyDTOPooled(ArrayList<BodyData> bodyDataList) {
        ArrayList<DynamicRenderDTO> renderables = new ArrayList<>();

        for (BodyData bodyData : bodyDataList) {
            DynamicRenderDTO dto = this.fromBodyDTOPooled(bodyData);

            if (dto != null) {
                renderables.add(dto);
            }
        }

        return renderables;
    }


    // *** INTERFACE IMPLEMENTATIONS ***

    // region DTOPooledMapper
    @Override
    protected boolean mapToDTO(Object source, DynamicRenderDTO target) {
        if (!(source instanceof BodyData)) {
            return false;
        }

        BodyData bodyData = (BodyData) source;
        PhysicsValuesMDTO phyValues = bodyData.getPhysicsValues();

        if (phyValues == null || bodyData.entityId == null) {
            return false;
        }

        target.updateFrom(
                bodyData.entityId,
                phyValues.posX, phyValues.posY,
                phyValues.angle,
                phyValues.size,
                phyValues.timeStamp,
                phyValues.speedX, phyValues.speedY,
                phyValues.accX, phyValues.accY,
                phyValues.timeStamp);

        return true;
    }
    // endregion
}