package com.mysema.query.sql;

import org.junit.Test;

import com.mysema.query.grammar.QMath;
import com.mysema.query.grammar.QString;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.sql.domain.QEMPLOYEE;

/**
 * HsqldbFunctionsTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HsqldbFunctionsTest {
    
    @Test
    public void testNumeric(){
        Expr<Integer> i = new Expr.EConstant<Integer>(1);
        Expr<Double> d = new Expr.EConstant<Double>(1.0);
//    ABS(d)        returns the absolute value of a double value
        QMath.abs(i);
//    ACOS(d)       returns the arc cosine of an angle
        QMath.acos(d);
//    ASIN(d)       returns the arc sine of an angle
        QMath.asin(d);
//    ATAN(d)       returns the arc tangent of an angle
        QMath.atan(d);
//    ATAN2(a,b)    returns the tangent of a/b
        
//    BITAND(a,b)   return a & b
        
//    BITOR(a,b)    returns a | b
        
//    CEILING(d)    returns the smallest integer that is not less than d
        QMath.ceil(d);
//    COS(d)        returns the cosine of an angle
        QMath.cos(d);
//    COT(d)        returns the cotangent of an angle
        
//    DEGREES(d)    converts radians to degrees
        
//    EXP(d)        returns e (2.718...) raised to the power of d
        QMath.exp(d);
//    FLOOR(d)      returns the largest integer that is not greater than d
        QMath.floor(d);
//    LOG(d)        returns the natural logarithm (base e)
        QMath.log(d);
//    LOG10(d)      returns the logarithm (base 10)
        QMath.log10(d);
//    MOD(a,b)      returns a modulo b
        QMath.mod(i,i);
//    PI()          returns pi (3.1415...)
        
//    POWER(a,b)    returns a raised to the power of b
        QMath.pow(d,d);
//    RADIANS(d)    converts degrees to radians
        
//    RAND()        returns a random number x bigger or equal to 0.0 and smaller than 1.0
        QMath.random();
//    ROUND(a,b)    rounds a to b digits after the decimal point
        QMath.round(d);
//    ROUNDMAGIC(d) solves rounding problems such as 3.11-3.1-0.01
        
//    SIGN(d)       returns -1 if d is smaller than 0, 0 if d==0 and 1 if d is bigger than 0
        
//    SIN(d)        returns the sine of an angle
        QMath.sin(d);        
//    SQRT(d)       returns the square root
        QMath.sqrt(i);
//    TAN(A)        returns the trigonometric tangent of an angle
        QMath.tan(d);
//    TRUNCATE(a,b) truncates a to b digits after the decimal point
    }   
     
    @Test
    public void testStringFunctions(){
        Expr.EString s = new QEMPLOYEE("e").firstname;
//
//    String built-in Functions / Stored Procedures
//
//    ASCII(s)              returns the ASCII code of the leftmost character of s
//    BIT_LENGTH(str)[2]    returns the length of the string in bits
//    CHAR(c)               returns a character that has the ASCII code c
//    CHAR_LENGTH(str)[2]   returns the length of the string in characters
//    CONCAT(str1,str2)     returns str1 + str2
        s.concat(s);
//    DIFFERENCE(s1,s2)     returns the difference between the sound of s1 and s2
//    HEXTORAW(s1)[2]       returns translated string
//    INSERT(s,start,len,s2)returns a string where len number of characters beginning at start has been replaced by s2
//    LCASE(s)              converts s to lower case
        s.lower();
//    LEFT(s,count)         returns the leftmost count of characters of s) - requires double quoting - use SUBSTRING() instead
//    LENGTH(s)             returns the number of characters in s
        QString.length(s);
//    LOCATE(search,s,[start])returns the first index (1=left, 0=not found) where search is found in s, starting at start
//    LTRIM(s)              removes all leading blanks in s
        QString.ltrim(s);
//    OCTET_LENGTH(str)[2]  returns the length of the string in bytes (twice the number of characters)
//    RAWTOHEX(s1)[2]       returns translated string
//    REPEAT(s,count)       returns s repeated count times
//    REPLACE(s,replace,s2) replaces all occurrences of replace in s with s2
//    RIGHT(s,count)        returns the rightmost count of characters of s
//    RTRIM(s)              removes all trailing spaces
        QString.rtrim(s);
//    SOUNDEX(s)            returns a four character code representing the sound of s
//    SPACE(count)          returns a string consisting of count spaces
        QString.space(4);
//    SUBSTR(s,start[,len]) alias for substring
        s.substring(1);
//    SUBSTRING(s,start[,len])  returns the substring starting at start (1=left) with length len        
//    UCASE(s)              converts s to upper case
        s.upper();
//    LOWER(s)              converts s to lower case
        s.lower();
//    UPPER(s)              converts s to upper case
        s.upper();
    }   
    
    @Test
    public void testDateTimeFunctions(){
//
//    Date/Time built-in Functions / Stored Procedures
//
//    CURDATE()             returns the current date
        
//    CURTIME()             returns the current time
//    DATEDIFF(string, datetime1, datetime2)[2]
//        returns the count of units of time elapsed from datetime1 to datetime2. The string indicates the unit of time and can have the following values 'ms'='millisecond', 'ss'='second','mi'='minute','hh'='hour', 'dd'='day', 'mm'='month', 'yy' = 'year'. Both the long and short form of the strings can be used.
//    DAYNAME(date)         returns the name of the day
//    DAYOFMONTH(date)      returns the day of the month (1-31)
//    DAYOFWEEK(date)       returns the day of the week (1 means Sunday)
//    DAYOFYEAR(date)       returns the day of the year (1-366)
//    HOUR(time)            return the hour (0-23)
//    MINUTE(time)          returns the minute (0-59)
//    MONTH(date)           returns the month (1-12)
//    MONTHNAME(date)       returns the name of the month
//    NOW()                 returns the current date and time as a timestamp) - use CURRENT_TIMESTAMP instead
//    QUARTER(date)         returns the quarter (1-4)
//    SECOND(time)          returns the second (0-59)
//    WEEK(date)            returns the week of this year (1-53)
//    YEAR(date)            returns the year
//    CURRENT_DATE[2]       returns the current date
//    CURRENT_TIME[2]       returns the current time
//    CURRENT_TIMESTAMP[2]  returns the current timestamp
    }   
    
    @Test
    public void testBuiltinFunctions(){
//
//    System built-in Functions / Stored Procedures
//
//    IFNULL(exp,value)
//        if exp is null, value is returned else exp) - use COALESCE() instead
//    CASEWHEN(exp,v1,v2)
//        if exp is true, v1 is returned, else v2) - use CASE WHEN instead
//    CONVERT(term,type)
//        converts exp to another data type
//    CAST(term AS type)[2]
//        converts exp to another data type
//    COALESCE(expr1,expr2,expr3,...)[2]
//        if expr1 is not null then it is returned else, expr2 is evaluated and if not null it is returned and so on
//    NULLIF(v1,v2)[2]
//        if v1 equals v2 return null, otherwise v1
//    CASE v1 WHEN...[2]
//        CASE v1 WHEN v2 THEN v3 [ELSE v4] END
//        when v1 equals v2 return v3 [otherwise v4 or null if there is no ELSE]
//    CASE WHEN...[2]
//        CASE WHEN expr1 THEN v1[WHEN expr2 THEN v2] [ELSE v4] END
//        when expr1 is true return v1 [optionally repeated for more cases] [otherwise v4 or null if there is no ELSE]
//    EXTRACT[2]
//        EXTRACT ({YEAR | MONTH | DAY | HOUR | MINUTE | SECOND} FROM <datetime value>)
//    POSITION (... IN ..)[2]
//        POSITION(<string expression> IN <string expression>)
//        if the first string is a sub-string of the second one, returns the position of the sub-string, counting from one; otherwise 0
//    SUBSTRING(... FROM ... FOR ...)[2]
//        SUBSTRING(<string expression> FROM <numeric expression> [FOR <numeric expression>]) 
//    TRIM( LEDING ... FROM ...)[2]
//        TRIM([{LEADING | TRAILING | BOTH}] FROM <string expression>) 
    }

}
