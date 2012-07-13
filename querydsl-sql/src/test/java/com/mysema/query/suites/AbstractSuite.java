package com.mysema.query.suites;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.mysema.query.BeanPopulationBase;
import com.mysema.query.Connections;
import com.mysema.query.DeleteBase;
import com.mysema.query.InsertBase;
import com.mysema.query.LikeEscapeBase;
import com.mysema.query.MergeBase;
import com.mysema.query.SelectBase;
import com.mysema.query.SubqueriesBase;
import com.mysema.query.TypesBase;
import com.mysema.query.UnionBase;
import com.mysema.query.UpdateBase;

// TODO classpath scan for classes instead
@RunWith(Suite.class)
@SuiteClasses({
    BeanPopulationBase.class,
    DeleteBase.class,
    InsertBase.class,
    LikeEscapeBase.class,
    MergeBase.class,
    SelectBase.class,
    SubqueriesBase.class,
    TypesBase.class,
    UnionBase.class,
    UpdateBase.class})
public abstract class AbstractSuite {
    
    @AfterClass
    public static void tearDown() throws SQLException {
        Connections.close();
    }
    
}