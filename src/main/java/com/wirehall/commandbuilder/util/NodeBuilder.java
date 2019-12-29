package com.wirehall.commandbuilder.util;

import com.wirehall.commandbuilder.dto.Command;
import com.wirehall.commandbuilder.dto.Flag;
import com.wirehall.commandbuilder.dto.Option;
import com.wirehall.commandbuilder.model.COMMAND_PROPERTY;
import com.wirehall.commandbuilder.model.DATA_TYPE;
import com.wirehall.commandbuilder.model.FLAG_PROPERTY;
import com.wirehall.commandbuilder.model.OPTION_PROPERTY;

import java.util.Map;

public class NodeBuilder {

    public static Command buildCommand(String name, String desc, String longDesc) {
        Command command = new Command();
        Map<COMMAND_PROPERTY, Object> props = command.getProperties();

        props.put(COMMAND_PROPERTY.name, name);
        props.put(COMMAND_PROPERTY.desc, desc);
        props.put(COMMAND_PROPERTY.long_desc, longDesc);

        return command;
    }

    public static void addFlag(Command c, String name, String alias, String prefix, String desc, String longDesc, int sequence) {
        Flag flag = new Flag();
        Map<FLAG_PROPERTY, Object> props = flag.getProperties();

        props.put(FLAG_PROPERTY.name, name);
        props.put(FLAG_PROPERTY.alias, alias);
        props.put(FLAG_PROPERTY.prefix, prefix);
        props.put(FLAG_PROPERTY.desc, desc);
        props.put(FLAG_PROPERTY.long_desc, longDesc);
        props.put(FLAG_PROPERTY.sequence, sequence);

        c.addFlag(flag);
    }

    public static void addOption(Command c, String name, String alias, String prefix, String desc, String longDesc,
                                 DATA_TYPE dataType, boolean isMandatory, boolean isRepeatable, int sequence) {
        Option option = new Option();
        Map<OPTION_PROPERTY, Object> props = option.getProperties();

        props.put(OPTION_PROPERTY.name, name);
        props.put(OPTION_PROPERTY.alias, alias);
        props.put(OPTION_PROPERTY.prefix, prefix);
        props.put(OPTION_PROPERTY.desc, desc);
        props.put(OPTION_PROPERTY.long_desc, longDesc);
        props.put(OPTION_PROPERTY.data_type, dataType.toString());
        props.put(OPTION_PROPERTY.is_mandatory, isMandatory);
        props.put(OPTION_PROPERTY.is_repeatable, isRepeatable);
        props.put(OPTION_PROPERTY.sequence, sequence);

        c.addOption(option);
    }


}
