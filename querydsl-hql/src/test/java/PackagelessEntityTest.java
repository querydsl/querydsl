import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.jpa.HQLSubQuery;
import com.mysema.query.types.path.PathBuilder;


public class PackagelessEntityTest {
    
    @SuppressWarnings("unchecked")
    @Test
    public void PackageLess_Path(){
        HQLSubQuery query = new HQLSubQuery();
        PathBuilder builder = new PathBuilder(PackagelessEntityTest.class,"entity");
        query.from(builder);
        assertEquals("from PackagelessEntityTest entity", query.toString());
    }

}
