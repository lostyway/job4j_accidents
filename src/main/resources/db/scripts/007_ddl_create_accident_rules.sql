create table accident_rules
(
    accident_id int references accidents (id),
    rule_id     int references rules (id),
    primary key (accident_id, rule_id)
);