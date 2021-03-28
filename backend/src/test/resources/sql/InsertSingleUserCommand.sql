insert into usercommand (command_name, command_text, timestamp, user_email, id) values('basename', 'basename -az  --suffix=test  /abc/xyz', CURRENT_TIMESTAMP, 'abc@xyz.com', 99)
insert into usercommand_flag (usercommand_id, name, value) values(99, 'z', true)
insert into usercommand_flag (usercommand_id, name, value) values(99, 'a', true)
insert into usercommand_option (usercommand_id, name, value) values(99, 'SUFFIX', '["test"]')
insert into usercommand_option (usercommand_id, name, value) values(99, 'FILE', '["/abc/xyz"]')