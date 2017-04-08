package test.io.github.scottbrand.common.cloner;

import io.github.scottbrand.common.ICloner;

public class TargetObject extends SourceObject implements ICloner<SourceObject>
{
    private SourceObject source;
    
    
    public TargetObject(SourceObject source)
    {
        super();
        clone(source);
    }
    
    
    @Override
    public SourceObject getSourceObject()
    {
        return source;
    }

    @Override
    public void setSourceObject(SourceObject source)
    {
        this.source = source;
    }

}
