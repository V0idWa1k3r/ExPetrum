package v0id.api.exp.entity;

/**
 * Created by V0idWa1k3r on 18-Jun-17.
 */
public enum EnumGender
{
    MALE,
    FEMALE;

    EnumGender()
    {

    }

    public EnumGender getOpposite()
    {
        return this == MALE ? FEMALE : MALE;
    }

    public boolean isOpposite(EnumGender other)
    {
        return this.getOpposite() == other;
    }
}
