package excel;

import util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class Meta
{
    private String displayName;
    private String name;
    private Map<String, String> languageNames = new HashMap<String, String>();

    public Meta(String name)
    {
        this.name = name;
    }

    public String getDisplayName()
    {
        //if (ReportUtil.isNullOrEmpty(displayName))
        if (StringUtil.isNullOrEmpty(displayName))
        {
            return name;
        }
        //String currLauguage = ThreadLocaleUtil.getLocaleName();
        String currLauguage = "zh_CN";
        String currName = languageNames.get(currLauguage);
        if (currName != null)
        {
            return currName;
        }

        String[] languages = displayName.split(";");

        for (String str : languages)
        {
            String[] nameTemp = str.split(":");
            languageNames.put(nameTemp[0], nameTemp[1]);
        }

        return languageNames.get(currLauguage);
    }

    public void setDisplayName(String displayName)
    {
        languageNames.clear();
        this.displayName = displayName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Meta other = (Meta)obj;
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        return true;
    }
}
