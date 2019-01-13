package VBA;

public class VBA {

//    Sub ASR_CARD()
//    t = Timer
//    Application.ScreenUpdating = False
//
//    Dim RegEx As Object
//    Set RegEx = CreateObject("VBScript.RegExp")
//    RegEx.IgnoreCase = True
//    RegEx.MultiLine = True
//    RegEx.Global = True
//
//    Dim d1 As Object
//    Set d1 = CreateObject("Scripting.Dictionary")
//
//    Dim folderlist(1), i, listlen, rootpath, resultfolder
//    Dim FSO As Object
//
//    Set FSO = CreateObject("Scripting.FileSystemObject")
//
//    Dim SH21 As Object
//    Set SH21 = Sheet21
//    SH21.Activate
//
//    If SH21.FilterMode = True Then SH21.ShowAllData
//
//            oldDataRows = SH21.UsedRange.Rows.count
//    If oldDataRows > 7 Then
//            NE_INFO = SH21.Range("A7:I" & (oldDataRows))
//
//    For i4 = 1 To UBound(NE_INFO)
//    If NE_INFO(i4, 2) <> "" And NE_INFO(i4, 1) <> "" Then
//    d3(NE_INFO(i4, 7)) = NE_INFO(i4, 2)
//    d3(NE_INFO(i4, 3)) = NE_INFO(i4, 2)
//    d4(NE_INFO(i4, 2)) = NE_INFO(i4, 3)
//    d5(NE_INFO(i4, 2)) = NE_INFO(i4, 2) & "|" & NE_INFO(i4, 3) & "|" & NE_INFO(i4, 5)
//    End If
//    Next
//    Erase NE_INFO
//    End If
//
//    Dim SH5 As Object
//    Set SH5 = Sheet5
//    SH5.Activate
//    With Application.FileDialog(msoFileDialogFolderPicker)
//        .Title = "Please select the folder to extract ARP&MAC"
//
//    If .Show = True Then
//            'MsgBox .SelectedItems(1)
//    rootpath = .SelectedItems(1)
//
//    Set rootfolder = FSO.getfolder(rootpath)
//            'MsgBox (rootfolder.Path)
//
//
//    For Each Folder In rootfolder.SubFolders
//            file_list = ""
//    NE_IP = Trim(Folder.Name)
//    NE_NAME = d3(NE_IP)
//    NE_INFO = d5(NE_NAME)
//
//    Set This_folder = FSO.getfolder(Folder.path)
//                'Set Files = this_folder.Files
//
//    src_file = Dir(This_folder & "\" & "*" & NE_IP & "*txt")
//            Do While src_file <> ""
//            sText = FSO.opentextfile(This_folder & "\" & src_file).ReadAll
//
//
//
//
//            SH27.Range("A10").Resize(UBound(ARR_STATUS, 1), UBound(ARR_STATUS, 2)).Value2 = ARR_STATUS
//     SH27.Rows("10:10").Copy
//     SH27.Rows("11:" & (UBound(ARR_STATUS, 1) + 15)).PasteSpecial Paste:=xlPasteFormats, Operation:=xlNone, SkipBlanks:=False, Transpose:=False
}
