import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import {
  deleteProfilePicture,
  getProfilePictureBlob,
  uploadProfilePicture,
} from "../api/users";
import { useAuth } from "../contexts";

const QUERY_KEY = ["profilePicture"] as const;

export const useProfilePicture = () => {
  const { getToken } = useAuth();

  return useQuery<Blob | null>({
    queryKey: QUERY_KEY,
    queryFn: getProfilePictureBlob,
    enabled: !!getToken(),
    staleTime: 5 * 60 * 1000,
    retry: false,
  });
};

export const useUploadProfilePicture = (onSuccess?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (file: File) => uploadProfilePicture(file),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: QUERY_KEY });
      onSuccess?.();
    },
  });
};

export const useDeleteProfilePicture = (
  onSuccess?: () => void,
  onError?: (error: unknown) => void
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteProfilePicture,
    onSuccess: () => {
      queryClient.setQueryData(QUERY_KEY, null);
      queryClient.invalidateQueries({ queryKey: QUERY_KEY });
      onSuccess?.();
    },
    onError: (error) => {
      onError?.(error);
    },
  });
};
