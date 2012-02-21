package utils;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class StringUtils
{
    public static boolean isEmpty(String s)
    {
        return s == null || s.trim().length() == 0;
    }
}
