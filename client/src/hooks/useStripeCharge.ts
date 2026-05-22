import { useMutation } from "@tanstack/react-query";
import { axiosApp } from "../api/axiosApp";
import type { ChargeRequest, PaymentResponse } from "../components/StripeForm";

const chargeCard = async (request: ChargeRequest): Promise<PaymentResponse> => {
  const response = await axiosApp.post("/payments/charge", request);
  return response.data;
};

export const useStripeCharge = (
  onSuccess?: (data: PaymentResponse) => void,
  onError?: (error: any) => void
) => {
  return useMutation<PaymentResponse, any, ChargeRequest>({
    mutationFn: chargeCard,
    onSuccess,
    onError,
  });
};
