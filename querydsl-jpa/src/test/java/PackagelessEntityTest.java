import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jpa.JPQLSubQuery;
import com.mysema.query.types.path.PathBuilder;


public class PackagelessEntityTest {
    
    @SuppressWarnings("unchecked")
    @Test
    public void PackageLess_Path(){
        JPQLSubQuery query = new JPQLSubQuery();
        PathBuilder builder = new PathBuilder(PackagelessEntityTest.class,"entity");
        query.from(builder);
        assertEquals("from PackagelessEntityTest entity", query.toString());
    }

}
