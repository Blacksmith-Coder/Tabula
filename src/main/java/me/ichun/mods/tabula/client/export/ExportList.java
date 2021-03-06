package me.ichun.mods.tabula.client.export;

import me.ichun.mods.ichunutil.common.module.tabula.formats.types.Exporter;
import me.ichun.mods.tabula.client.export.types.ExportJava;
import me.ichun.mods.tabula.client.export.types.ExportProjectTexture;
import me.ichun.mods.tabula.client.export.types.ExportTextureMap;

import java.util.ArrayList;

public final class ExportList
{
    public static final ArrayList<Exporter> exportTypes = new ArrayList<Exporter>() {{
        add(new ExportTextureMap());
        add(new ExportJava());
        add(new ExportProjectTexture());
    }};
}
