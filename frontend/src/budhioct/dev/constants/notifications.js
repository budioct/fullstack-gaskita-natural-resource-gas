import { useToast } from "vue-toast-notification";

const toast = useToast();

export const useNotification = {
    success(title, message) {
        toast.success(`<b>${title}:</b> ${message}`, {
            position: "top",
            duration: 9000,
            dismissible: true,
        });
    },
    error(title, message) {
        toast.error(`<b>${title}:</b> ${message}`, {
            position: "top",
            duration: 9000,
            dismissible: true,
        });
    },
    info(title, message) {
        toast.info(`<b>${title}:</b> ${message}`, {
            position: "top",
            duration: 9000,
            dismissible: true,
        });
    },
    warning(title, message) {
        toast.warning(`<b>${title}:</b> ${message}`, {
            position: "top",
            duration: 9000,
            dismissible: true,
        });
    },
};
