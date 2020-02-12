import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import Backend from 'i18next-xhr-backend';
import LanguageDetector from 'i18next-browser-languagedetector';

i18n
  // load translation using xhr -> see /public/locales
  .use(Backend)
  // detect user language
  .use(LanguageDetector)
  // pass the i18n instance to react-i18next.
  .use(initReactI18next)
  // init i18next
  .init({
    fallbackLng: 'sr',
    debug: true,

    lng: 'sr',
    backend: {
      /* translation file path */
      loadPath: '/locales//{{lng}}/translation.json'
    },
    keySeparator: false,

    interpolation: {
      escapeValue: false, // not needed for react as it escapes by default
      formatSeparator: ','
    },

    react: {
      wait: true
    }
  });

export default i18n;