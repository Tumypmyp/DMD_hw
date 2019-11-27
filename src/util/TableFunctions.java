package util;

import java.sql.Connection;
import java.sql.Statement;

public class TableFunctions {

    public static void initialisation(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("create table staff_position(\n" +
                    "                               position_id integer not null, -- ID of the position\n" +
                    "                               position_name varchar(30) not null,\n" +
                    "                               position_access_level integer not null, -- Access level of the position\n" +
                    "                               position_salary money,\n" +
                    "                               WH_start time, -- Start of the working Hours\n" +
                    "                               WH_end time, -- End of the working Hours\n" +
                    "                               primary key(position_id)\n" +
                    ");");
            statement.execute("CREATE TYPE gender_type AS ENUM ('male', 'female');");
            statement.execute("create table staff(\n" +
                    "                      sid integer not null, -- staff's ID\n" +
                    "                      first_name varchar(30) not null,\n" +
                    "                      last_name varchar(30) not null,\n" +
                    "                      date_of_birth date,\n" +
                    "                      gender gender_type,\n" +
                    "                      position_id integer not null, -- Position of the staff's member\n" +
                    "                      email varchar(50) not null,\n" +
                    "                      address varchar(100),\n" +
                    "                      profile_image varchar(100), -- Path to the profile's image\n" +
                    "                      primary key(sid),\n" +
                    "                      foreign key(position_id) references staff_position(position_id)\n" +
                    ");");
            statement.execute("create table staff_contacts(\n" +
                    "                               sid integer not null, -- Staff's ID\n" +
                    "                               phone_number bigint not null,\n" +
                    "                               primary key(sid, phone_number),\n" +
                    "                               foreign key(sid) references staff(sid)\n" +
                    ");");
            statement.execute("create table portfolio(\n" +
                    "                          sid integer not null,\n" +
                    "                          description text, -- description\n" +
                    "                          skills text, -- skills\n" +
                    "                          achievements text, -- achievements\n" +
                    "                          electronic_copy varchar(100), -- path to the electronic copy\n" +
                    "                          loading_date date,\n" +
                    "                          comments text, -- comments\n" +
                    "                          access_level integer not null,\n" +
                    "                          primary key(sid),\n" +
                    "                          foreign key(sid) references staff(sid)\n" +
                    ");");
            statement.execute("create table previous_positions(\n" +
                    "                                   sid integer not null,\n" +
                    "                                   place varchar(100),\n" +
                    "                                   position varchar(30),\n" +
                    "                                   work_began date not null,\n" +
                    "                                   work_finished date,\n" +
                    "                                   employer_feedback text,\n" +
                    "                                   primary key (sid, place, position),\n" +
                    "                                   foreign key (sid) references staff(sid)\n" +
                    ");");
            statement.execute("create table patients(\n" +
                    "                         pid integer not null,\n" +
                    "                         first_name varchar(30),\n" +
                    "                         last_name varchar(30),\n" +
                    "                         date_of_birth date,\n" +
                    "                         gender gender_type,\n" +
                    "                         status varchar(10),\n" +
                    "                         email varchar(100),\n" +
                    "                         address varchar(100),\n" +
                    "                         profile_image varchar(100), --path to profile image\n" +
                    "                         stats text,\n" +
                    "                         comments text,\n" +
                    "                         primary key(pid)\n" +
                    ");");
            statement.execute("create table patient_contacts(\n" +
                    "                                 pid integer not null,\n" +
                    "                                 phone_number bigint not null,\n" +
                    "                                 primary key(pid, phone_number),\n" +
                    "                                 foreign key(pid) references patients(pid)\n" +
                    ");");
            statement.execute("create table medical_history(\n" +
                    "                                pid integer not null,\n" +
                    "                                description text,\n" +
                    "                                time_loaded timestamp,\n" +
                    "                                last_modified timestamp,\n" +
                    "                                access_level integer not null,\n" +
                    "                                electronic_copy varchar(100),\n" +
                    "                                comments text,\n" +
                    "                                primary key(pid),\n" +
                    "                                foreign key(pid) references patients(pid)\n" +
                    ");");
            statement.execute("create table patients_illnesses(\n" +
                    "                                   pid integer not null,\n" +
                    "                                   illness varchar(100) not null,\n" +
                    "                                   illness_start date,\n" +
                    "                                   illness_finish date,\n" +
                    "                                   therapist_id integer,\n" +
                    "                                   comments text,\n" +
                    "                                   primary key(pid, illness, illness_start, illness_finish),\n" +
                    "                                   foreign key(pid) references patients(pid),\n" +
                    "                                   foreign key(therapist_id) references staff(sid)\n" +
                    ");");
            statement.execute("create table inventory_categories(\n" +
                    "                                     purpose varchar(30) not null, -- The purpose of the item\n" +
                    "                                     stored_in varchar(100) not null, -- The place where the item is stored\n" +
                    "                                     access_level integer,\n" +
                    "                                     primary key(purpose)\n" +
                    ");");
            statement.execute("create table inventory(\n" +
                    "                          item_id integer not null, -- ID of the item in the table\n" +
                    "                          purpose varchar(30) not null,\n" +
                    "                          item_name varchar(50) not null,\n" +
                    "                          item_description text,\n" +
                    "                          booked_by integer,\n" +
                    "                          primary key(item_id),\n" +
                    "                          foreign key(purpose) references inventory_categories(purpose),\n" +
                    "                          foreign key(booked_by) references staff\n" +
                    ");");
            statement.execute("CREATE TYPE appointment_type_id AS ENUM ('home_visit', 'buiseness_meeting', 'hospital_visit');");
            statement.execute("create table appointments(\n" +
                    "                             appointment_id integer not null,\n" +
                    "                             appointment_type appointment_type_id not null,\n" +
                    "                             appointment_time timestamp not null,\n" +
                    "                             place varchar(100) not null,\n" +
                    "                             primary key(appointment_id)\n" +
                    ");");
            statement.execute("create table staff_in_appointment(\n" +
                    "                                     appointment_id integer not null,\n" +
                    "                                     staff_member integer not null,\n" +
                    "                                     primary key(appointment_id, staff_member),\n" +
                    "                                     foreign key (appointment_id) references appointments(appointment_id),\n" +
                    "                                     foreign key (staff_member) references staff(sid)\n" +
                    ");");
            statement.execute("create table patients_in_appointment(\n" +
                    "                                        appointment_id integer not null,\n" +
                    "                                        patient_member integer not null,\n" +
                    "                                        primary key(appointment_id, patient_member),\n" +
                    "                                        foreign key (appointment_id) references appointments(appointment_id),\n" +
                    "                                        foreign key (patient_member) references patients(pid)\n" +
                    ");");
            statement.execute("create table applicants(\n" +
                    "                           applicant_id integer not null,\n" +
                    "                           first_name varchar(30) not null,\n" +
                    "                           last_name varchar(30) not null,\n" +
                    "                           gender gender_type,\n" +
                    "                           date_of_birth date,\n" +
                    "                           phone_number bigint,\n" +
                    "                           email varchar(100),\n" +
                    "                           profile_image varchar(100), -- path to the profile image\n" +
                    "                           status varchar(10),\n" +
                    "                           primary key(applicant_id)\n" +
                    ");");
            statement.execute("create table cvs(\n" +
                    "                    applicant_id integer not null,\n" +
                    "                    electronic_copy varchar(100) not null,\n" +
                    "                    loaded timestamp not null,\n" +
                    "                    last_modified timestamp not null,\n" +
                    "                    access_level integer not null,\n" +
                    "                    primary key(applicant_id),\n" +
                    "                    foreign key(applicant_id) references applicants(applicant_id)\n" +
                    ");");
            statement.execute("create table applicants_previous_positions(\n" +
                    "                                              applicant_id integer not null,\n" +
                    "                                              org_name varchar(100) not null,\n" +
                    "                                              position varchar(100) not null,\n" +
                    "                                              org_description text,\n" +
                    "                                              position_description text,\n" +
                    "                                              work_began date not null,\n" +
                    "                                              work_finished date,\n" +
                    "                                              employee_feedback text,\n" +
                    "                                              primary key(applicant_id, org_name, position),\n" +
                    "                                              foreign key(applicant_id) references applicants(applicant_id)\n" +
                    ");");
            statement.execute("create table available_positions(\n" +
                    "                                    position_id integer not null,\n" +
                    "                                    position_name varchar(100) not null,\n" +
                    "                                    position_description text,\n" +
                    "                                    position_requirenments text,\n" +
                    "                                    salary money,\n" +
                    "                                    primary key(position_id)\n" +
                    ");");
            statement.execute("create table motivation_letters(\n" +
                    "                                   MLID integer not null,\n" +
                    "                                   electronic_copy varchar(100), -- Path to the copy of the motivation letter\n" +
                    "                                   loaded timestamp not null,\n" +
                    "                                   last_modified timestamp not null,\n" +
                    "                                   access_level integer not null,\n" +
                    "                                   primary key(MLID)\n" +
                    ");");
            statement.execute("create table applies_for(\n" +
                    "                            position_id integer not null,\n" +
                    "                            applicant_id integer not null,\n" +
                    "                            MLID integer not null,\n" +
                    "                            primary key(position_id,applicant_id, MLID),\n" +
                    "                            foreign key(position_id) references available_positions(position_id),\n" +
                    "                            foreign key(applicant_id) references applicants(applicant_id),\n" +
                    "                            foreign key(MLID) references motivation_letters(MLID)\n" +
                    ");");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clear(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("drop table if exists applies_for;\n" +
                    "drop table if exists motivation_letters;\n" +
                    "drop table if exists available_positions;\n" +
                    "drop table if exists applicants_previous_positions;\n" +
                    "drop table if exists cvs;\n" +
                    "drop table if exists applicants;\n" +
                    "drop table if exists patient_contacts;\n" +
                    "drop table if exists medical_history;\n" +
                    "drop table if exists staff_in_appointment;\n" +
                    "drop table if exists patients_in_appointment;\n" +
                    "drop table if exists appointments;\n" +
                    "drop table if exists appointment_type;\n" +
                    "drop table if exists patients_illnesses;\n" +
                    "drop table if exists patients;\n" +
                    "drop table if exists inventory;\n" +
                    "drop table if exists inventory_categories;\n" +
                    "drop table if exists staff_contacts;\n" +
                    "drop table if exists portfolio;\n" +
                    "drop table if exists previous_positions;\n" +
                    "drop table if exists staff;\n" +
                    "drop table if exists staff_position;\n" +
                    "drop type if exists gender_type;\n" +
                    "drop type if exists appointment_type_id;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
