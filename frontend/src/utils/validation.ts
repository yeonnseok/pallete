export const validateOnlyNumber = (
  message: string,
  value?: string
): Promise<void> => {
  if (!validateStringNumber(value)) {
    return Promise.reject(new Error(message as string));
  }
  return Promise.resolve();
};

export const validateStringNumber = (value: string | undefined): boolean =>
  !value || !!value && /^[0-9, ]+$/.test(value);
