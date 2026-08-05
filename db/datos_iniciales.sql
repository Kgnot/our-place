-- =====================================================================
-- SEED DATA — Catálogos maestros (lkp_*) y planes de negocio
-- No incluye datos de clientes/usuarios reales.
-- =====================================================================

-- ---------------------------------------------------------------------
-- identity.lkp_user_status
-- ---------------------------------------------------------------------
INSERT INTO "identity"."lkp_user_status" ("code", "name") VALUES
                                                              ('active',                 'Activo'),
                                                              ('pending_verification',   'Pendiente de verificación'),
                                                              ('disabled',               'Deshabilitado'),
                                                              ('locked',                 'Bloqueado');

-- ---------------------------------------------------------------------
-- identity.lkp_contact_type
-- ---------------------------------------------------------------------
INSERT INTO "identity"."lkp_contact_type" ("code", "name") VALUES
                                                               ('email',    'Correo electrónico'),
                                                               ('phone',    'Teléfono'),
                                                               ('whatsapp', 'WhatsApp');

-- ---------------------------------------------------------------------
-- identity.lkp_role
-- ---------------------------------------------------------------------
INSERT INTO "identity"."lkp_role" ("code", "name") VALUES
                                                       ('room_owner',  'Propietario del espacio'),
                                                       ('room_member', 'Miembro del espacio'),
                                                       ('room_guest',  'Invitado del espacio');

-- ---------------------------------------------------------------------
-- billing.plan (datos de la empresa: planes comerciales)
-- ---------------------------------------------------------------------
INSERT INTO "billing"."plan"
("code", "name", "storage_quota_bytes", "max_members", "price_cents", "billing_period", "is_active")
VALUES
    ('free',      'Gratuito',   2147483648,  2,     0, NULL,       true),
    ('basic',     'Básico',     21474836480, 4,  990000, 'monthly', true),
    ('premium',   'Premium',    107374182400, 6, 2490000, 'monthly', true),
    ('family',    'Familiar',   214748364800, 10, 3990000, 'monthly', true),
    ('legacy_v1', 'Legado V1',  5368709120,  4,  790000, 'monthly', false);

-- ---------------------------------------------------------------------
-- room.lkp_room_status
-- ---------------------------------------------------------------------
INSERT INTO "room"."lkp_room_status" ("code", "name") VALUES
                                                          ('active',    'Activo'),
                                                          ('archived',  'Archivado'),
                                                          ('suspended', 'Suspendido'),
                                                          ('deleted',   'Eliminado');

-- ---------------------------------------------------------------------
-- room.lkp_relationship_type
-- ---------------------------------------------------------------------
INSERT INTO "room"."lkp_relationship_type" ("code", "name") VALUES
                                                                ('couple',        'Pareja'),
                                                                ('family',         'Familia'),
                                                                ('friends',        'Amigos'),
                                                                ('engaged',        'Comprometidos'),
                                                                ('married',        'Casados'),
                                                                ('long_distance',  'Relación a distancia'),
                                                                ('other',          'Otro');

-- ---------------------------------------------------------------------
-- gallery.lkp_media_type
-- ---------------------------------------------------------------------
INSERT INTO "gallery"."lkp_media_type" ("code", "name") VALUES
                                                            ('photo', 'Foto'),
                                                            ('video', 'Video'),
                                                            ('audio', 'Audio');

-- ---------------------------------------------------------------------
-- gallery.lkp_processing_status
-- ---------------------------------------------------------------------
INSERT INTO "gallery"."lkp_processing_status" ("code", "name") VALUES
                                                                   ('pending',    'Pendiente'),
                                                                   ('processing', 'Procesando'),
                                                                   ('completed',  'Completado'),
                                                                   ('failed',     'Fallido');

-- ---------------------------------------------------------------------
-- map.lkp_place_category
-- ---------------------------------------------------------------------
INSERT INTO "map"."lkp_place_category" ("code", "name", "icon_url") VALUES
                                                                        ('restaurant',   'Restaurante',       NULL),
                                                                        ('cafe',         'Café',              NULL),
                                                                        ('park',         'Parque',            NULL),
                                                                        ('hotel',        'Hotel',             NULL),
                                                                        ('beach',        'Playa',             NULL),
                                                                        ('landmark',     'Lugar emblemático', NULL),
                                                                        ('bar',          'Bar',               NULL),
                                                                        ('cinema',       'Cine',              NULL),
                                                                        ('shopping',     'Centro comercial',  NULL),
                                                                        ('other',        'Otro',              NULL);

-- ---------------------------------------------------------------------
-- notification.lkp_notification_type
-- ---------------------------------------------------------------------
INSERT INTO "notification"."lkp_notification_type" ("code", "name") VALUES
                                                                        ('room_invitation',      'Invitación a espacio'),
                                                                        ('new_media',            'Nueva foto o video'),
                                                                        ('media_reaction',       'Reacción a contenido'),
                                                                        ('media_comment',        'Comentario en contenido'),
                                                                        ('day_entry_created',    'Nueva entrada del calendario'),
                                                                        ('storage_quota_alert',  'Alerta de cuota de almacenamiento'),
                                                                        ('member_joined',        'Nuevo miembro en el espacio'),
                                                                        ('anniversary_reminder', 'Recordatorio de aniversario');