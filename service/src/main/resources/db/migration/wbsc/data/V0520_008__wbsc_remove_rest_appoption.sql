--remove all REST appoptions

DELETE FROM wbsc.appoptions WHERE key = 'REST_API_AUDIO_PATH';
DELETE FROM wbsc.appoptions WHERE key = 'REST_API_VIDEO_PATH';
DELETE FROM wbsc.appoptions WHERE key = 'REST_API_XML_PATH';
DELETE FROM wbsc.appoptions WHERE key = 'REST_API_UNIVERSAL_PLAYER';
DELETE FROM wbsc.appoptions WHERE key = 'REST_API_UNIVERSAL_COMMUNICATOR_CONTEXT';
