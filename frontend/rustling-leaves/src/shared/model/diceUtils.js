export function getOptionValue(option) {
    return Number(option.slice(-1));
}

export function isOptionClouded(option) {
    return option.startsWith("OPTION_CLOUDED");
}