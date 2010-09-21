package com.mysema.query.scala

import com.mysema.query.jpa.impl.JPAQuery
import com.mysema.query.scala.Conversions._

import javax.persistence._;
import org.junit.{Test,Before,After};
import org.junit.Assert._

import scala.collection.JavaConversions._

import scala.reflect.BeanProperty;

class DumpClassTest {

    @Test
    def test(){
        List(classOf[U], classOf[D], classOf[C]).foreach(cl => 
        {
              println(cl.getName);
              cl.getDeclaredFields.foreach( f =>  {
                  println(f.getName);
                  println(" " + java.util.Arrays.asList(f.getAnnotations:_*));
                  
              } );
              println();
              cl.getDeclaredMethods.foreach( m =>  {
                  println(m.getName);
                  println(" " + java.util.Arrays.asList(m.getReturnType.getAnnotations:_*));
              } );
              println();
        });
    }
}

@Entity
class U(   @BeanProperty @Id        var id: Integer,
           @BeanProperty            var userName: String,
           @BeanProperty @ManyToOne var department: Department)

@Entity
class D(

    @BeanProperty
    @Id
    var id: Integer,
    
    @BeanProperty
    var name: String,
    
    @ManyToOne
    @BeanProperty
    var company: C
)

@Entity
class C (
 
    @BeanProperty
    @Id
    var id : Integer,
    
    @BeanProperty
    var name: String,
    
    @BeanProperty
    @OneToMany(mappedBy="company")
    var departments: java.util.Set[D]
    
    
)