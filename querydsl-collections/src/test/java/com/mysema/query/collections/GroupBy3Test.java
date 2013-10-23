package com.mysema.query.collections;

import static com.mysema.query.group.GroupBy.groupBy;
import static com.mysema.query.group.GroupBy.set;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.commons.lang.EmptyCloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.group.Group;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QTuple;

public class GroupBy3Test {

    @QueryEntity
    public static class RiskAnalysis {
        public String id;
        public Set<AssetThreat> assetThreats;
    }

    @QueryEntity
    public static class AssetThreat {
        public String id;
        public Set<Threat> threats;
    }

    @QueryEntity
    public static class Threat {
        public String id;
    }

    @Test
    public void Nested_Expressions() {
        QGroupBy3Test_RiskAnalysis riskAnalysis = QGroupBy3Test_RiskAnalysis.riskAnalysis;
        QGroupBy3Test_AssetThreat assetThreat = QGroupBy3Test_AssetThreat.assetThreat;
        QGroupBy3Test_Threat threat = QGroupBy3Test_Threat.threat;

        ResultTransformer<Map<String,RiskAnalysis>> transformer =
                groupBy(riskAnalysis.id)
                  .as(Projections.bean(RiskAnalysis.class,
                          riskAnalysis.id,
                          set(Projections.bean(AssetThreat.class,
                              assetThreat.id,
                              set(Projections.bean(Threat.class, threat.id)).as("threats")))
                          .as("assetThreats")));

        Projectable projectable = createMock(Projectable.class);
        expect(projectable.iterate(new QTuple(
                riskAnalysis.id,
                riskAnalysis.id,
                assetThreat.id,
                Projections.bean(Threat.class, threat.id))))
            .andReturn(new EmptyCloseableIterator());
        replay(projectable);

        transformer.transform(projectable);
        verify(projectable);
    }

    @Test
    public void Alias_Usage() {
        QGroupBy3Test_RiskAnalysis riskAnalysis = QGroupBy3Test_RiskAnalysis.riskAnalysis;
        QGroupBy3Test_AssetThreat assetThreat = QGroupBy3Test_AssetThreat.assetThreat;
        QGroupBy3Test_Threat threat = QGroupBy3Test_Threat.threat;

        ResultTransformer<Map<String,Group>> transformer =
                groupBy(riskAnalysis.id)
                  .as(riskAnalysis.id,
                      set(Projections.bean(AssetThreat.class,
                          assetThreat.id,
                           set(Projections.bean(Threat.class, threat.id)).as("threats"))
                          .as("assetThreats")));

        Projectable projectable = createMock(Projectable.class);
        expect(projectable.iterate(new QTuple(
                riskAnalysis.id,
                riskAnalysis.id,
                assetThreat.id,
                Projections.bean(Threat.class, threat.id))))
            .andReturn(new EmptyCloseableIterator());
        replay(projectable);

        transformer.transform(projectable);
        verify(projectable);
    }

}
