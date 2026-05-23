import { axiosApp } from "./axiosApp";

export const downloadTicketPdf = async (ticketId: string): Promise<Blob> => {
  const response = await axiosApp.get(`/tickets/${ticketId}/pdf`, {
    responseType: "blob",
  });
  return response.data as Blob;
};