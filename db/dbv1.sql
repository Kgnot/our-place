CREATE EXTENSION Postgis;

CREATE SCHEMA "identity";

CREATE SCHEMA "billing";

CREATE SCHEMA "room";

CREATE SCHEMA "gallery";

CREATE SCHEMA "calendar";

CREATE SCHEMA "map";

CREATE SCHEMA "notification";

CREATE TABLE "identity"."lkp_user_status" (
                                              "code" varchar(30) PRIMARY KEY,
                                              "name" varchar(100) NOT NULL
);

CREATE TABLE "identity"."lkp_contact_type" (
                                               "code" varchar(20) PRIMARY KEY,
                                               "name" varchar(50) NOT NULL
);

CREATE TABLE "identity"."lkp_role" (
                                       "code" varchar(30) PRIMARY KEY,
                                       "name" varchar(100) NOT NULL
);

CREATE TABLE "identity"."users_login" (
                                          "id" uuid PRIMARY KEY,
                                          "email" varchar(255) UNIQUE NOT NULL,
                                          "password_hash" varchar(255),
                                          "auth_provider" varchar(20) NOT NULL DEFAULT 'local',
                                          "provider_user_id" varchar(255),
                                          "status_code" varchar(30) NOT NULL,
                                          "mfa_enabled" boolean NOT NULL DEFAULT false,
                                          "failed_login_attempts" smallint NOT NULL DEFAULT 0,
                                          "locked_until" timestamptz,
                                          "password_reset_token" varchar(255),
                                          "password_reset_expires_at" timestamptz,
                                          "last_login_at" timestamptz,
                                          "created_at" timestamptz NOT NULL,
                                          "updated_at" timestamptz NOT NULL
);

CREATE TABLE "identity"."profile" (
                                      "user_login_id" uuid PRIMARY KEY,
                                      "first_name" varchar(100) NOT NULL,
                                      "last_name" varchar(100),
                                      "avatar_url" varchar(500),
                                      "birth_date" date,
                                      "timezone" varchar(50) DEFAULT 'America/Bogota',
                                      "locale" varchar(10) DEFAULT 'es-CO',
                                      "created_at" timestamptz NOT NULL
);

CREATE TABLE "identity"."user_contact" (
                                           "user_login_id" uuid,
                                           "contact_type_code" varchar(20),
                                           "value" varchar(255) NOT NULL,
                                           "is_primary" boolean NOT NULL DEFAULT false,
                                           "is_verified" boolean NOT NULL DEFAULT false,
                                           "created_at" timestamptz NOT NULL,
                                           PRIMARY KEY ("user_login_id", "contact_type_code", "value")
);

CREATE TABLE "billing"."plan" (
                                  "code" varchar(30) PRIMARY KEY,
                                  "name" varchar(100) NOT NULL,
                                  "storage_quota_bytes" bigint NOT NULL,
                                  "max_members" smallint NOT NULL DEFAULT 2,
                                  "price_cents" integer NOT NULL DEFAULT 0,
                                  "billing_period" varchar(20),
                                  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "billing"."room_subscription" (
                                               "room_id" uuid PRIMARY KEY,
                                               "plan_code" varchar(30) NOT NULL,
                                               "status" varchar(20) NOT NULL DEFAULT 'active',
                                               "storage_used_bytes" bigint NOT NULL DEFAULT 0,
                                               "started_at" timestamptz NOT NULL,
                                               "current_period_end" timestamptz
);

CREATE TABLE "room"."lkp_room_status" (
                                          "code" varchar(30) PRIMARY KEY,
                                          "name" varchar(100) NOT NULL
);

CREATE TABLE "room"."lkp_relationship_type" (
                                                "code" varchar(40) PRIMARY KEY,
                                                "name" varchar(100) NOT NULL
);

CREATE TABLE "room"."rooms" (
                                "id" uuid PRIMARY KEY,
                                "name" varchar(150) NOT NULL,
                                "status_code" varchar(30) NOT NULL,
                                "relationship_type_code" varchar(40),
                                "owner_user_id" uuid NOT NULL,
                                "anniversary_date" date,
                                "timezone" varchar(50) DEFAULT 'America/Bogota',
                                "created_at" timestamptz NOT NULL
);

CREATE TABLE "room"."room_member" (
                                      "room_id" uuid,
                                      "user_login_id" uuid,
                                      "role_code" varchar(30) NOT NULL,
                                      "status" varchar(20) NOT NULL DEFAULT 'active',
                                      "invited_by_user_id" uuid,
                                      "nickname" varchar(50),
                                      "joined_at" timestamptz NOT NULL,
                                      PRIMARY KEY ("room_id", "user_login_id")
);

CREATE TABLE "room"."room_invitation" (
                                          "id" bigserial PRIMARY KEY,
                                          "room_id" uuid NOT NULL,
                                          "invited_email" varchar(255) NOT NULL,
                                          "invited_by_user_id" uuid NOT NULL,
                                          "role_code" varchar(30) NOT NULL,
                                          "token" varchar(255) UNIQUE NOT NULL,
                                          "status" varchar(20) NOT NULL DEFAULT 'pending',
                                          "expires_at" timestamptz NOT NULL,
                                          "created_at" timestamptz NOT NULL,
                                          "accepted_at" timestamptz
);

CREATE TABLE "room"."member_relationship" (
                                              "room_id" uuid,
                                              "member_a_user_id" uuid,
                                              "member_b_user_id" uuid,
                                              "relationship_type_code" varchar(40) NOT NULL,
                                              "since_date" date,
                                              "created_at" timestamptz NOT NULL,
                                              PRIMARY KEY ("room_id", "member_a_user_id", "member_b_user_id")
);

CREATE TABLE "gallery"."lkp_media_type" (
                                            "code" varchar(20) PRIMARY KEY,
                                            "name" varchar(50) NOT NULL
);

CREATE TABLE "gallery"."lkp_processing_status" (
                                                   "code" varchar(30) PRIMARY KEY,
                                                   "name" varchar(100) NOT NULL
);

CREATE TABLE "gallery"."media" (
                                   "id" uuid PRIMARY KEY,
                                   "room_id" uuid NOT NULL,
                                   "uploaded_by_user_id" uuid NOT NULL,
                                   "r2_url" varchar(500) NOT NULL,
                                   "thumbnail_url" varchar(500),
                                   "media_type_code" varchar(20) NOT NULL,
                                   "processing_status_code" varchar(30) NOT NULL,
                                   "retry_count" smallint NOT NULL DEFAULT 0,
                                   "error_message" text,
                                   "file_size_bytes" bigint,
                                   "mime_type" varchar(100),
                                   "caption" text,
                                   "taken_at" timestamptz,
                                   "location" geography,
                                   "saved_place_id" uuid,
                                   "exif_raw_payload" jsonb,
                                   "deleted_at" timestamptz,
                                   "purge_at" timestamptz,
                                   "created_at" timestamptz NOT NULL
);

CREATE TABLE "gallery"."media_reaction" (
                                            "media_id" uuid,
                                            "user_login_id" uuid,
                                            "reaction_type" varchar(20) NOT NULL DEFAULT 'heart',
                                            "created_at" timestamptz NOT NULL,
                                            PRIMARY KEY ("media_id", "user_login_id")
);

CREATE TABLE "gallery"."media_comment" (
                                           "id" bigserial PRIMARY KEY,
                                           "media_id" uuid NOT NULL,
                                           "user_login_id" uuid NOT NULL,
                                           "content" text NOT NULL,
                                           "created_at" timestamptz NOT NULL,
                                           "deleted_at" timestamptz
);

CREATE TABLE "calendar"."day_entry" (
                                        "room_id" uuid,
                                        "entry_date" date NOT NULL,
                                        "created_by_user_id" uuid,
                                        "content" text,
                                        "mood_emoji" varchar(10),
                                        "created_at" timestamptz NOT NULL,
                                        PRIMARY KEY ("room_id", "entry_date")
);

CREATE TABLE "calendar"."day_entry_media" (
                                              "room_id" uuid,
                                              "entry_date" date,
                                              "media_id" uuid,
                                              "created_at" timestamptz NOT NULL,
                                              PRIMARY KEY ("room_id", "entry_date", "media_id")
);

CREATE TABLE "map"."lkp_place_category" (
                                            "code" varchar(30) PRIMARY KEY,
                                            "name" varchar(100) NOT NULL,
                                            "icon_url" varchar(255)
);

CREATE TABLE "map"."saved_place" (
                                     "id" uuid PRIMARY KEY,
                                     "room_id" uuid NOT NULL,
                                     "created_by_user_id" uuid NOT NULL,
                                     "category_code" varchar(30),
                                     "name" varchar(150) NOT NULL,
                                     "description" text,
                                     "location" geography NOT NULL,
                                     "is_auto_generated" boolean NOT NULL DEFAULT false,
                                     "source_media_id" uuid,
                                     "visited_at" date,
                                     "created_at" timestamptz NOT NULL
);

CREATE TABLE "map"."location_ping" (
                                       "id" bigserial PRIMARY KEY,
                                       "user_login_id" uuid NOT NULL,
                                       "room_id" uuid NOT NULL,
                                       "location" geography NOT NULL,
                                       "battery_level" smallint,
                                       "recorded_at" timestamptz NOT NULL
);

CREATE TABLE "notification"."lkp_notification_type" (
                                                        "code" varchar(40) PRIMARY KEY,
                                                        "name" varchar(100) NOT NULL
);

CREATE TABLE "notification"."notification" (
                                               "id" bigserial PRIMARY KEY,
                                               "room_id" uuid NOT NULL,
                                               "recipient_user_id" uuid NOT NULL,
                                               "actor_user_id" uuid,
                                               "type_code" varchar(40) NOT NULL,
                                               "entity_type" varchar(30),
                                               "entity_id" varchar(64),
                                               "is_read" boolean NOT NULL DEFAULT false,
                                               "created_at" timestamptz NOT NULL
);

CREATE UNIQUE INDEX ON "identity"."users_login" ("auth_provider", "provider_user_id");

CREATE INDEX ON "room"."room_invitation" ("room_id", "invited_email", "status");

CREATE INDEX ON "gallery"."media" ("room_id", "taken_at");

CREATE INDEX ON "gallery"."media" ("room_id", "processing_status_code");

CREATE INDEX ON "gallery"."media" ("room_id", "deleted_at");

CREATE INDEX ON "gallery"."media_comment" ("media_id", "created_at");

CREATE INDEX ON "map"."location_ping" ("room_id", "recorded_at");

CREATE INDEX ON "map"."location_ping" ("user_login_id", "recorded_at");

CREATE INDEX ON "notification"."notification" ("recipient_user_id", "is_read", "created_at");

COMMENT ON TABLE "identity"."lkp_user_status" IS 'CODE natural: active, pending_verification, disabled, locked';

COMMENT ON TABLE "identity"."lkp_contact_type" IS 'CODE natural: email, phone, whatsapp';

COMMENT ON TABLE "identity"."lkp_role" IS 'CODE natural: room_owner, room_member, room_guest.
Vive en identity aunque solo lo consuma "room" porque es catálogo
de identidad/permisos, no dato operativo de una sala específica.
';

COMMENT ON TABLE "identity"."users_login" IS 'password_hash nullable: a futuro habrá login solo-OAuth (Google/Apple) sin password local';

COMMENT ON COLUMN "identity"."users_login"."id" IS 'UUID: viaja en el JWT y en cookies de sesión; no debe ser adivinable/enumerable';

COMMENT ON COLUMN "identity"."users_login"."status_code" IS 'FK real -> identity.lkp_user_status.code (mismo schema)';

COMMENT ON TABLE "identity"."profile" IS 'FK real -> identity.users_login.id (mismo schema, PK=FK: relación 1:1)';

COMMENT ON COLUMN "identity"."profile"."user_login_id" IS 'COMPUESTA/1:1: mismo id que users_login.id, es su tabla de extensión (no necesita un id propio)';

COMMENT ON TABLE "identity"."user_contact" IS 'COMPUESTA: la fila ES "este usuario tiene este valor de contacto de este tipo".
Un trigger debe garantizar solo 1 is_primary=true por (user_login_id, contact_type_code).
';

COMMENT ON COLUMN "identity"."user_contact"."user_login_id" IS 'FK real -> identity.profile.user_login_id (mismo schema)';

COMMENT ON COLUMN "identity"."user_contact"."contact_type_code" IS 'FK real -> identity.lkp_contact_type.code (mismo schema)';

COMMENT ON TABLE "billing"."plan" IS 'CODE natural: free, couple_premium, family... max_members permite crecer de 2 a N sin cambiar esquema';

COMMENT ON TABLE "billing"."room_subscription" IS 'status: active, trialing, past_due, canceled. storage_used_bytes es caché actualizado por job.';

COMMENT ON COLUMN "billing"."room_subscription"."room_id" IS 'COMPUESTA/1:1 (una sala tiene máximo una suscripción activa) +
SIN FK real (cross-schema) -> room.rooms.id.
billing no depende del motor de FK de "room" para existir/leerse.
';

COMMENT ON COLUMN "billing"."room_subscription"."plan_code" IS 'FK real -> billing.plan.code (mismo schema)';

COMMENT ON TABLE "room"."lkp_room_status" IS 'CODE natural: active, trial, suspended';

COMMENT ON TABLE "room"."lkp_relationship_type" IS 'CODE natural: partner, family, friend';

COMMENT ON TABLE "room"."rooms" IS 'Tenant principal. anniversary_date es dato central para el timeline de pareja.';

COMMENT ON COLUMN "room"."rooms"."id" IS 'UUID: id de tenant, referenciado desde muchos schemas y potencialmente en URLs futuras';

COMMENT ON COLUMN "room"."rooms"."status_code" IS 'FK real -> room.lkp_room_status.code (mismo schema)';

COMMENT ON COLUMN "room"."rooms"."relationship_type_code" IS 'FK real -> room.lkp_relationship_type.code (mismo schema)';

COMMENT ON COLUMN "room"."rooms"."owner_user_id" IS 'SIN FK (cross-schema) -> identity.users_login.id. Desacople identity<->room.';

COMMENT ON TABLE "room"."room_member" IS 'COMPUESTA: la fila ES "este usuario pertenece a esta sala".
role_code vive aquí (no en identity) porque el rol es por sala.
status: invited, active, left.
';

COMMENT ON COLUMN "room"."room_member"."room_id" IS 'FK real -> room.rooms.id (mismo schema)';

COMMENT ON COLUMN "room"."room_member"."user_login_id" IS 'SIN FK (cross-schema) -> identity.users_login.id. Desacople identity<->room.';

COMMENT ON COLUMN "room"."room_member"."role_code" IS 'SIN FK (cross-schema) -> identity.lkp_role.code. Mismo motivo.';

COMMENT ON COLUMN "room"."room_member"."invited_by_user_id" IS 'SIN FK (cross-schema) -> identity.users_login.id.';

COMMENT ON TABLE "room"."room_invitation" IS 'invited_email sin FK a propósito: la persona invitada puede no tener
cuenta aún en identity.users_login. status: pending, accepted, expired, revoked.
';

COMMENT ON COLUMN "room"."room_invitation"."id" IS 'BIGSERIAL: id interno de sistema; el identificador público real que viaja en el email es `token`';

COMMENT ON COLUMN "room"."room_invitation"."room_id" IS 'FK real -> room.rooms.id (mismo schema)';

COMMENT ON COLUMN "room"."room_invitation"."invited_by_user_id" IS 'SIN FK (cross-schema) -> identity.users_login.id.';

COMMENT ON COLUMN "room"."room_invitation"."role_code" IS 'SIN FK (cross-schema) -> identity.lkp_role.code.';

COMMENT ON TABLE "room"."member_relationship" IS 'COMPUESTA: la fila ES "estos dos miembros de esta sala tienen esta relación".
Solo relevante al escalar a N miembros (ej: madre-hijo, hermanos).
Para pareja (2 personas) actual, usar rooms.relationship_type_code /
rooms.anniversary_date directamente y dejar esta tabla vacía.
En Postgres real: ALTER TABLE ... ADD FOREIGN KEY (room_id, member_a_user_id)
REFERENCES room.room_member (room_id, user_login_id) -- válido contra PK compuesta,
aunque no se representa como Ref formal en este diagrama.
';

COMMENT ON COLUMN "room"."member_relationship"."room_id" IS 'FK real -> room.rooms.id (mismo schema)';

COMMENT ON COLUMN "room"."member_relationship"."member_a_user_id" IS 'SIN FK real declarada: referencia lógica a (room_id, user_login_id) de room.room_member; se valida por constraint manual/aplicación porque apunta a una PK compuesta';

COMMENT ON COLUMN "room"."member_relationship"."member_b_user_id" IS 'Mismo caso que member_a_user_id';

COMMENT ON COLUMN "room"."member_relationship"."relationship_type_code" IS 'FK real -> room.lkp_relationship_type.code (mismo schema)';

COMMENT ON TABLE "gallery"."lkp_media_type" IS 'CODE natural: image, video';

COMMENT ON TABLE "gallery"."lkp_processing_status" IS 'CODE natural: pending, processing, completed, failed (Cloudflare Queues)';

COMMENT ON TABLE "gallery"."media" IS 'deleted_at = papelera (soft delete); purge_at = borrado definitivo, gestionado por job periódico';

COMMENT ON COLUMN "gallery"."media"."id" IS 'UUID: contenido íntimo/privado — un id secuencial permitiría intentar enumerar fotos de otras salas si hay un bug de autorización';

COMMENT ON COLUMN "gallery"."media"."room_id" IS 'SIN FK (cross-schema) -> room.rooms.id. Desacople room<->gallery.';

COMMENT ON COLUMN "gallery"."media"."uploaded_by_user_id" IS 'SIN FK (cross-schema) -> identity.users_login.id.';

COMMENT ON COLUMN "gallery"."media"."media_type_code" IS 'FK real -> gallery.lkp_media_type.code (mismo schema)';

COMMENT ON COLUMN "gallery"."media"."processing_status_code" IS 'FK real -> gallery.lkp_processing_status.code (mismo schema)';

COMMENT ON COLUMN "gallery"."media"."saved_place_id" IS 'SIN FK (cross-schema) -> map.saved_place.id. Nullable: se completa async vía job de EXIF.';

COMMENT ON TABLE "gallery"."media_reaction" IS 'COMPUESTA: la fila ES "este usuario reaccionó a esta foto" (1 reacción por usuario por foto)';

COMMENT ON COLUMN "gallery"."media_reaction"."media_id" IS 'FK real -> gallery.media.id (mismo schema)';

COMMENT ON COLUMN "gallery"."media_reaction"."user_login_id" IS 'SIN FK (cross-schema) -> identity.users_login.id.';

COMMENT ON COLUMN "gallery"."media_comment"."id" IS 'BIGSERIAL: alto volumen, orden de inserción importa para mostrar el hilo, no se comparte por URL propia';

COMMENT ON COLUMN "gallery"."media_comment"."media_id" IS 'FK real -> gallery.media.id (mismo schema)';

COMMENT ON COLUMN "gallery"."media_comment"."user_login_id" IS 'SIN FK (cross-schema) -> identity.users_login.id.';

COMMENT ON TABLE "calendar"."day_entry" IS 'COMPUESTA (llave de negocio natural): una sala tiene como máximo
una entrada por fecha. content nullable: puede ser solo fotos vinculadas,
solo texto, o ambos.
';

COMMENT ON COLUMN "calendar"."day_entry"."room_id" IS 'SIN FK (cross-schema) -> room.rooms.id. Desacople room<->calendar.';

COMMENT ON COLUMN "calendar"."day_entry"."created_by_user_id" IS 'SIN FK (cross-schema) -> identity.users_login.id.';

COMMENT ON TABLE "calendar"."day_entry_media" IS 'COMPUESTA: puente N:N puro entre calendar.day_entry y gallery.media.
FK real (room_id, entry_date) -> calendar.day_entry (room_id, entry_date).
';

COMMENT ON COLUMN "calendar"."day_entry_media"."room_id" IS 'Parte de la FK compuesta hacia calendar.day_entry (mismo schema)';

COMMENT ON COLUMN "calendar"."day_entry_media"."entry_date" IS 'Parte de la FK compuesta hacia calendar.day_entry (mismo schema)';

COMMENT ON COLUMN "calendar"."day_entry_media"."media_id" IS 'SIN FK (cross-schema) -> gallery.media.id. Desacople gallery<->calendar.';

COMMENT ON TABLE "map"."lkp_place_category" IS 'CODE natural: restaurant, park, hotel, first_date';

COMMENT ON TABLE "map"."saved_place" IS 'is_auto_generated=true cuando se creó desde el EXIF de una foto; false si fue manual';

COMMENT ON COLUMN "map"."saved_place"."id" IS 'UUID: ubicaciones/direcciones privadas — evita enumeración de lugares de otras salas';

COMMENT ON COLUMN "map"."saved_place"."room_id" IS 'SIN FK (cross-schema) -> room.rooms.id. Desacople room<->map.';

COMMENT ON COLUMN "map"."saved_place"."created_by_user_id" IS 'SIN FK (cross-schema) -> identity.users_login.id.';

COMMENT ON COLUMN "map"."saved_place"."category_code" IS 'FK real -> map.lkp_place_category.code (mismo schema)';

COMMENT ON COLUMN "map"."saved_place"."source_media_id" IS 'SIN FK (cross-schema) -> gallery.media.id. Puede quedar huérfano si la foto se purga; la app lo ignora en ese caso.';

COMMENT ON TABLE "map"."location_ping" IS 'Última ubicación conocida de cada usuario = MAX(recorded_at) por user_login_id. Purgado por job periódico.';

COMMENT ON COLUMN "map"."location_ping"."id" IS 'BIGSERIAL: escritura de altísima frecuencia (ping continuo desde la app móvil), ventana de retención 24h; el rendimiento de inserción importa más que un id no-secuencial';

COMMENT ON COLUMN "map"."location_ping"."user_login_id" IS 'SIN FK (cross-schema) -> identity.users_login.id.';

COMMENT ON COLUMN "map"."location_ping"."room_id" IS 'SIN FK (cross-schema) -> room.rooms.id.';

COMMENT ON TABLE "notification"."lkp_notification_type" IS 'CODE natural: media_uploaded, media_comment_added, media_reaction_added, day_entry_added, place_added, member_joined';

COMMENT ON COLUMN "notification"."notification"."id" IS 'BIGSERIAL: alto volumen de eventos, orden de inserción define el feed, nunca se comparte por URL propia';

COMMENT ON COLUMN "notification"."notification"."room_id" IS 'SIN FK (cross-schema) -> room.rooms.id.';

COMMENT ON COLUMN "notification"."notification"."recipient_user_id" IS 'SIN FK (cross-schema) -> identity.users_login.id.';

COMMENT ON COLUMN "notification"."notification"."actor_user_id" IS 'SIN FK (cross-schema) -> identity.users_login.id.';

COMMENT ON COLUMN "notification"."notification"."type_code" IS 'FK real -> notification.lkp_notification_type.code (mismo schema)';

COMMENT ON COLUMN "notification"."notification"."entity_id" IS 'Referencia POLIMÓRFICA sin FK: entity_type indica la tabla (media, day_entry,
saved_place...) y entity_id su identificador. Se guarda como texto porque
las tablas referenciadas usan tipos de PK distintos (uuid vs compuesta):
para day_entry se guarda como "room_id:entry_date" serializado.
';

ALTER TABLE "identity"."users_login" ADD FOREIGN KEY ("status_code") REFERENCES "identity"."lkp_user_status" ("code") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."profile" ADD FOREIGN KEY ("user_login_id") REFERENCES "identity"."users_login" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."user_contact" ADD FOREIGN KEY ("user_login_id") REFERENCES "identity"."profile" ("user_login_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."user_contact" ADD FOREIGN KEY ("contact_type_code") REFERENCES "identity"."lkp_contact_type" ("code") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "billing"."room_subscription" ADD FOREIGN KEY ("plan_code") REFERENCES "billing"."plan" ("code") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "room"."rooms" ADD FOREIGN KEY ("status_code") REFERENCES "room"."lkp_room_status" ("code") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "room"."rooms" ADD FOREIGN KEY ("relationship_type_code") REFERENCES "room"."lkp_relationship_type" ("code") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "room"."room_member" ADD FOREIGN KEY ("room_id") REFERENCES "room"."rooms" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "room"."room_invitation" ADD FOREIGN KEY ("room_id") REFERENCES "room"."rooms" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "room"."member_relationship" ADD FOREIGN KEY ("room_id") REFERENCES "room"."rooms" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "room"."member_relationship" ADD FOREIGN KEY ("relationship_type_code") REFERENCES "room"."lkp_relationship_type" ("code") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "gallery"."media" ADD FOREIGN KEY ("media_type_code") REFERENCES "gallery"."lkp_media_type" ("code") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "gallery"."media" ADD FOREIGN KEY ("processing_status_code") REFERENCES "gallery"."lkp_processing_status" ("code") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "gallery"."media_reaction" ADD FOREIGN KEY ("media_id") REFERENCES "gallery"."media" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "gallery"."media_comment" ADD FOREIGN KEY ("media_id") REFERENCES "gallery"."media" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "calendar"."day_entry_media" ADD FOREIGN KEY ("room_id", "entry_date") REFERENCES "calendar"."day_entry" ("room_id", "entry_date") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "map"."saved_place" ADD FOREIGN KEY ("category_code") REFERENCES "map"."lkp_place_category" ("code") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "notification"."notification" ADD FOREIGN KEY ("type_code") REFERENCES "notification"."lkp_notification_type" ("code") DEFERRABLE INITIALLY IMMEDIATE;
